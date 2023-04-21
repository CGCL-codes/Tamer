package org.fudaa.dodico.crue.projet.rename;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.fudaa.ctulu.CtuluLog;
import org.fudaa.dodico.crue.common.BusinessMessages;
import org.fudaa.dodico.crue.metier.CrueLevelType;
import org.fudaa.dodico.crue.metier.emh.EnumTypeEMH;
import org.fudaa.dodico.crue.metier.etude.EMHProjet;
import org.fudaa.dodico.crue.metier.etude.FichierCrue;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHContainer;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHContainerBase;
import org.fudaa.dodico.crue.projet.select.SelectableFinder.SelectedItems;

/**
 *
 * @author Chris
 */
public class RenameManager {

    public static final NumberFormat FORMATTER = NumberFormat.getIntegerInstance();

    public static class NewNameInformations {

        public Map<String, ManagerEMHContainerBase> newNameContainers = new HashMap<String, ManagerEMHContainerBase>();

        public Map<String, FichierCrue> newNameFiles = new HashMap<String, FichierCrue>();

        public boolean isEmpty() {
            return (this.newNameContainers.size() + this.newNameFiles.size()) == 0;
        }
    }

    private EMHProjet project;

    static {
        FORMATTER.setMinimumIntegerDigits(2);
    }

    public EMHProjet getProject() {
        return project;
    }

    public void setProject(EMHProjet project) {
        this.project = project;
    }

    public NewNameInformations getNewNames(ManagerEMHContainerBase container, SelectedItems selectedItems, String newRadical, boolean deep) {
        final NewNameInformations infos = new NewNameInformations();
        if (selectedItems.getSelectedContainers().contains(container)) {
            infos.newNameContainers.put(container.getLevel().getPrefix() + newRadical, container);
        }
        if (deep) {
            if (container.getListeFichiers() != null) {
                for (FichierCrue file : container.getListeFichiers().getFichiers()) {
                    if (selectedItems.getSelectedFiles().contains(file)) {
                        infos.newNameFiles.put(newRadical + "." + file.getType().getExtension(), file);
                    }
                }
            }
            this.computeNewChildrenNames(container, newRadical, infos, selectedItems);
        }
        return infos;
    }

    public NewNameInformations getNewNames(FichierCrue file, String newRadical) {
        final NewNameInformations infos = new NewNameInformations();
        infos.newNameFiles.put(newRadical + "." + file.getType().getExtension(), file);
        return infos;
    }

    private void computeNewChildrenNames(ManagerEMHContainerBase container, String newRadical, NewNameInformations informations, SelectedItems selectedItems) {
        final List<ManagerEMHContainerBase> children = (container instanceof ManagerEMHContainer<?>) ? (List<ManagerEMHContainerBase>) ((ManagerEMHContainer<?>) container).getFils() : null;
        boolean uniqueChild = children == null || children.size() == 1;
        int currentIdx = 1;
        if (children != null) {
            for (ManagerEMHContainerBase child : children) {
                if (selectedItems.getSelectedContainers().contains(child)) {
                    String newName = newRadical;
                    if (!uniqueChild) {
                        newName += FORMATTER.format(currentIdx++);
                    }
                    informations.newNameContainers.put(child.getLevel().getPrefix() + newName, child);
                    this.computeNewChildrenNames(child, newName, informations, selectedItems);
                }
            }
        }
        if (container.getListeFichiers() != null) {
            for (FichierCrue file : container.getListeFichiers().getFichiers()) {
                if (selectedItems.getSelectedFiles().contains(file)) {
                    informations.newNameFiles.put(newRadical + "." + file.getType().getExtension(), file);
                }
            }
        }
    }

    public CtuluLog canUseNewNames(NewNameInformations infos) {
        CtuluLog log = new CtuluLog();
        for (Map.Entry<String, ManagerEMHContainerBase> newName : infos.newNameContainers.entrySet()) {
            if (project.getContainer(newName.getKey(), newName.getValue().getLevel()) != null) {
                log.addError(BusinessMessages.getString("RenameManager.ManagerNameUsed", newName.getKey()));
            }
        }
        for (Map.Entry<String, FichierCrue> newName : infos.newNameFiles.entrySet()) {
            if ((project.getInfos().getFileFromBase(newName.getKey()) != null)) {
                log.addError(BusinessMessages.getString("RenameManager.FileNameUsed", newName.getKey()));
            }
        }
        return log;
    }

    public List<String> getOverwrittenFiles(NewNameInformations infos) {
        List<String> path = new ArrayList<String>();
        for (Entry<String, FichierCrue> entry : infos.newNameFiles.entrySet()) {
            FichierCrue old = entry.getValue();
            final String newName = entry.getKey();
            if (!newName.equals(old.getNom())) {
                FichierCrue newFile = old.clone();
                newFile.setNom(newName);
                final File file = newFile.getProjectFile(project);
                if (file.isFile()) {
                    path.add(file.getAbsolutePath());
                }
            }
        }
        return path;
    }

    public boolean rename(NewNameInformations infos, boolean overwriteFilesIfExists) {
        boolean result = true;
        for (Entry<String, ManagerEMHContainerBase> newName : infos.newNameContainers.entrySet()) {
            result &= this.project.renameManager(RenameManager.convert(newName.getValue().getLevel()), newName.getValue(), newName.getKey());
        }
        for (Entry<String, FichierCrue> newName : infos.newNameFiles.entrySet()) {
            result &= this.project.renameFile(newName.getValue(), newName.getKey(), overwriteFilesIfExists);
        }
        return result;
    }

    private static EnumTypeEMH convert(CrueLevelType type) {
        switch(type) {
            case SCENARIO:
                {
                    return EnumTypeEMH.SCENARIO;
                }
            case MODELE:
                {
                    return EnumTypeEMH.MODELE;
                }
            case SOUS_MODELE:
                {
                    return EnumTypeEMH.SOUS_MODELE;
                }
        }
        return null;
    }
}
