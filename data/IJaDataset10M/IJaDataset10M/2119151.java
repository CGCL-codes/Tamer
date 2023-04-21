package com.loribel.commons.business.metamodel.impl;

import java.io.File;
import com.loribel.commons.business.metamodel.GB_BOBOMetaDataContainer;
import com.loribel.commons.business.metamodel.GB_BOBOMetaDataContainerMgr;
import com.loribel.commons.business.metamodel.GB_BOBOMetaDataContainerSet;
import com.loribel.commons.io.GB_DirTools;
import com.loribel.commons.util.CTools;

/**
 * Default implementation of GB_BOBOMetaDataContainerMgr.
 *
 * @author Gregory Borelli
 */
public class GB_BOBOMetaDataContainerMgrImpl implements GB_BOBOMetaDataContainerMgr {

    private GB_BOBOMetaDataContainers boContainers;

    public synchronized void addContainer(GB_BOBOMetaDataContainer a_boContainer) {
        if (boContainers == null) {
            boContainers = new GB_BOBOMetaDataContainers();
        }
        boContainers.addContainer(a_boContainer);
    }

    public GB_BOBOMetaDataContainerSet addContainerFromFile(File a_file) {
        GB_BOBOMetaDataContainerSet l_boContainer = new GB_BOBOMetaDataContainerFile(a_file);
        addContainer(l_boContainer);
        return l_boContainer;
    }

    public GB_BOBOMetaDataContainer addContainerFromResource(Class a_loader, String a_resource) {
        GB_BOBOMetaDataContainer l_boContainer = new GB_BOBOMetaDataContainerResource(a_loader, a_resource);
        addContainer(l_boContainer);
        return l_boContainer;
    }

    public GB_BOBOMetaDataContainer[] addContainersFromDir(File a_dir, boolean a_deep) {
        File[] l_files = GB_DirTools.listFilesByExtension(a_dir, "xml", a_deep);
        int len = CTools.getSize(l_files);
        GB_BOBOMetaDataContainer[] retour = new GB_BOBOMetaDataContainer[len];
        for (int i = 0; i < len; i++) {
            File l_file = l_files[i];
            retour[i] = addContainerFromFile(l_file);
        }
        return retour;
    }

    public synchronized GB_BOBOMetaDataContainers getContainers() {
        return boContainers;
    }

    public synchronized boolean removeContainer(GB_BOBOMetaDataContainer a_boContainer) {
        if (boContainers == null) {
            return false;
        }
        boContainers.removeContainer(a_boContainer);
        return true;
    }
}
