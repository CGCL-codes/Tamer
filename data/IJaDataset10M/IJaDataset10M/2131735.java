package fr.insa.rennes.pelias.pcreator.editors.chains.models;

import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertySource;
import fr.insa.rennes.pelias.framework.Chain;
import fr.insa.rennes.pelias.pcreator.editors.ChainEditor;
import fr.insa.rennes.pelias.pcreator.editors.chains.SavedComponent;
import fr.insa.rennes.pelias.pcreator.editors.chains.propertysources.ChainPropertySource;

/**
 * Modèle d'une chaîne
 * @author Julien
 *
 */
public class ChainModel extends CompositeElement {

    /**
	 * Propriétés étroitement liées à la chaine représentée par le modèle.
	 * Ces constantes sont utilisées par ServicePropertySource pour l'affichage
	 * dans un propertyView;
	 */
    public static final String PROPERTY_LABEL = "ChainName";

    public static final String PROPERTY_DESC = "ChainDescription";

    public static final String PROPERTY_KEEPVERSION = "ChaineKeepVersion";

    private Chain chain;

    /**
	 * Propriété de la chaine définissant si une sauvegarde prochaine devra être faite
	 */
    private boolean isDirty;

    private boolean isMinor;

    private boolean isMajor;

    public ChainModel(Chain c) {
        chain = c;
        resetDirty();
    }

    public Chain getContent() {
        return chain;
    }

    @Override
    public boolean addChild(Element child) {
        boolean hasBeenAdded = super.addChild(child);
        if (hasBeenAdded) {
            if (child instanceof ChainInputModel) {
                ChainInputModel input = (ChainInputModel) child;
                getContent().getInputs().add(input.getContent());
                setMajor();
            } else if (child instanceof ChainOutputModel) {
                ChainOutputModel output = (ChainOutputModel) child;
                getContent().getOutputs().add(output.getContent());
                setMajor();
            }
            setMinor();
        }
        return hasBeenAdded;
    }

    @Override
    public boolean removeChild(Element child) {
        if (child instanceof SubChainModel) {
            ((SubChainModel) child).deleteConnections();
        } else if (child instanceof ServiceModel) {
            ((ServiceModel) child).deleteConnections();
        } else if (child instanceof ChainInputModel) {
            ChainInputModel input = (ChainInputModel) child;
            input.deleteConnections();
            chain.getInputs().remove(input.getContent());
            setMajor();
        } else if (child instanceof ChainOutputModel) {
            ChainOutputModel output = (ChainOutputModel) child;
            output.deleteConnections();
            chain.getOutputs().remove(output.getContent());
            setMajor();
        }
        setMinor();
        return super.removeChild(child);
    }

    public boolean addPipe(EndpointModel source, EndpointModel target) {
        if (source != null && target != null) {
            new PipeModel(source, target);
            setMinor();
            return true;
        }
        return false;
    }

    public boolean addPrecedence(ChainComponentModel source, ChainComponentModel target) {
        if (source != null && target != null) {
            new PrecedenceModel(source, target);
            setMinor();
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public String getAttachment() {
        ArrayList<SavedComponent> inputs = new ArrayList<SavedComponent>();
        ArrayList<SavedComponent> services = new ArrayList<SavedComponent>();
        ArrayList<SavedComponent> outputs = new ArrayList<SavedComponent>();
        ArrayList<SavedComponent> comments = new ArrayList<SavedComponent>();
        for (Element e : getChildren()) {
            if (e instanceof ChainInputModel) {
                inputs.add(((ChainInputModel) e).getSavedComponent());
            } else if (e instanceof ServiceModel) {
                services.add(((ServiceModel) e).getSavedComponent());
            } else if (e instanceof SubChainModel) {
                services.add(((SubChainModel) e).getSavedComponent());
            } else if (e instanceof ChainOutputModel) {
                outputs.add(((ChainOutputModel) e).getSavedComponent());
            } else if (e instanceof CommentModel) {
                comments.add(((CommentModel) e).getSavedComponent());
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(outputStream);
        encoder.setPersistenceDelegate(SavedComponent.class, new DefaultPersistenceDelegate(new String[] { "referencedClass", "id", "x", "y", "height", "width", "prop" }));
        for (SavedComponent i : inputs) encoder.writeObject(i);
        for (SavedComponent s : services) encoder.writeObject(s);
        for (SavedComponent o : outputs) encoder.writeObject(o);
        for (SavedComponent c : comments) encoder.writeObject(c);
        encoder.close();
        return outputStream.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == IPropertySource.class) return new ChainPropertySource(this);
        return null;
    }

    public boolean getMajor() {
        return isMajor;
    }

    public void setMajor() {
        isMajor = true;
        setDirty();
    }

    public void setDirty() {
        isDirty = true;
        IEditorPart curr = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (curr != null && curr instanceof ChainEditor) ((ChainEditor) curr).dirtyChanged();
    }

    public boolean getMinor() {
        return isMinor;
    }

    public void setMinor() {
        isMinor = true;
        setDirty();
    }

    public void resetDirty() {
        isDirty = false;
        isMinor = false;
        isMajor = false;
        IEditorPart curr = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (curr != null && curr instanceof ChainEditor) ((ChainEditor) curr).dirtyChanged();
    }

    public boolean isDirty() {
        return isDirty;
    }
}
