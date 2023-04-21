package mimosa.ontology.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import mimosa.editor.DefaultEditorFactory;
import mimosa.editor.GObject;
import mimosa.editor.GraphicsFactory;
import mimosa.ontology.Category;
import mimosa.ontology.CategoryDescription;
import mimosa.ontology.Individual;
import mimosa.ontology.TypeContainer;
import mimosa.probe.OutputFactory;
import mimosa.table.TableChangeEvent;
import mimosa.table.TableChangeListener;
import mimosa.table.dictionary.DictionaryEntry;
import mimosa.util.ObjectCreationPane;
import mimosa.util.ObjectEditor;

/**
 *
 * @author Jean-Pierre Muller
 */
public class IndividualEditorFactory extends DefaultEditorFactory implements TableChangeListener<String> {

    private static MIndividualEditor entityEditor = new MIndividualEditor();

    private static IndividualPane entityPane = new IndividualPane();

    private static LinkEdgePane linkPane = new LinkEdgePane();

    private static OutputEditor outputEditor = new OutputEditor();

    private static OutputPane outputPane = new OutputPane();

    private static OutputEdgePane outputEdgePane = new OutputEdgePane();

    private TypeContainer entityTypes;

    /**
	 * 
	 */
    public IndividualEditorFactory(GraphicsFactory graphicsFactory) {
        super(graphicsFactory);
        outputPane.setVisualizers(OutputFactory.getProbeOutputs());
        outputEditor.setVisualizers(OutputFactory.getProbeOutputs());
    }

    /**
	 * Sets the container of entity type definitions for the pane.
	 * @param entityTypes
	 */
    public void setEntityTypes(TypeContainer entityTypes) {
        this.entityTypes = entityTypes;
        entityTypes.addTableChangeListener(this);
        updateEntityTypeList();
    }

    /**
	 * Updates the pane list of available entity types.
	 */
    private void updateEntityTypeList() {
        List<Category> list = new Vector<Category>();
        for (Object entityType : entityTypes.getValues()) {
            Category category = (Category) entityType;
            if (!category.isAbstract()) list.add(category);
        }
        Collections.sort(list, new Comparator<DictionaryEntry>() {

            public int compare(DictionaryEntry o1, DictionaryEntry o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        entityPane.setPossibleTypes(list);
    }

    /**
	 * @see mimosa.editor.DefaultEditorFactory#getEditor(java.lang.Object)
	 */
    public ObjectEditor getEditor(Object value) {
        ObjectEditor editor = super.getEditor(value);
        if (editor == null) if (value instanceof IndividualMNode) return entityEditor; else if (value instanceof OutputMNode) return outputEditor; else return null; else return editor;
    }

    /**
	 * @see mimosa.editor.DefaultEditorFactory#getKind(java.lang.Object)
	 */
    public Object getKind(Object object) {
        if (object instanceof Individual) {
            return new CategoryDescription(((Individual) object).getType());
        } else return null;
    }

    /**
	 * @see mimosa.editor.DefaultEditorFactory#getKinds()
	 */
    public Collection<Object> getKinds() {
        if (entityTypes == null) return null;
        Collection<String> list = entityTypes.getKeys();
        List<Object> descriptions = new Vector<Object>();
        for (String name : list) descriptions.add(new CategoryDescription((Category) entityTypes.get(name)));
        return descriptions;
    }

    /**
	 * @see mimosa.editor.DefaultEditorFactory#getPane(java.lang.Object)
	 */
    public ObjectCreationPane getPane(Object kind) {
        ObjectCreationPane pane = super.getPane(kind);
        if (pane == null) {
            GObject prototype = graphicsFactory.getPrototype(((Integer) kind).intValue());
            if (prototype instanceof IndividualGNode) {
                return entityPane;
            } else if (prototype instanceof LinkGEdge) {
                return linkPane;
            } else if (prototype instanceof OutputGNode) {
                return outputPane;
            } else if (prototype instanceof OutputGEdge) {
                return outputEdgePane;
            } else return null;
        } else return pane;
    }

    /**
	 * @see mimosa.table.TableChangeListener#tableKeyAdded(mimosa.table.TableChangeEvent)
	 */
    public void tableKeyAdded(TableChangeEvent<String> evt) {
        updateEntityTypeList();
    }

    /**
	 * @see mimosa.table.TableChangeListener#tableKeyChanged(mimosa.table.TableChangeEvent)
	 */
    public void tableKeyChanged(TableChangeEvent<String> evt) {
        updateEntityTypeList();
    }

    /**
	 * @see mimosa.table.TableChangeListener#tableKeyRemoved(mimosa.table.TableChangeEvent)
	 */
    public void tableKeyRemoved(TableChangeEvent<String> evt) {
        updateEntityTypeList();
    }

    /**
	 * @see mimosa.table.TableChangeListener#tableValueChanged(mimosa.table.TableChangeEvent)
	 */
    public void tableValueChanged(TableChangeEvent<String> evt) {
        updateEntityTypeList();
    }
}
