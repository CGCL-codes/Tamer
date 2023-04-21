package uk.ac.essex.ia.opview;

import com.jgraph.JGraph;
import org.apache.log4j.Category;
import uk.ac.essex.common.gui.ApplicationFileImpl;
import uk.ac.essex.common.lang.LanguageManager;
import uk.ac.essex.ia.graph.jgraph.IAJGraph;
import uk.ac.essex.ia.lang.LanguageConstants;
import uk.ac.essex.ia.lang.LanguageManagerImpl;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <br>
 * Date: Apr 13, 2002 <br>
 *
 * @author Laurence Smith
 *         You should have received a copy of GNU public license with this code.
 *         If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class OperationFile extends ApplicationFileImpl<IAJGraph> {

    /** The log4j logger */
    private static transient Category category = Category.getInstance(OperationFile.class);

    protected IAJGraph graph;

    private LanguageManager translator = LanguageManagerImpl.getInstance();

    private boolean modified;

    private MyMouseAdapter mouseAdapter = new MyMouseAdapter();

    /**
     *
     */
    public OperationFile() {
        init();
    }

    /**
     *
     */
    public OperationFile(IAJGraph data) {
        super();
        category.debug("of");
        this.data = data;
        init();
    }

    /**
     *
     */
    public IAJGraph getGraph() {
        return (IAJGraph) data;
    }

    /** Sets the data object if this is not the required type nothing is done */
    public void setData(IAJGraph data) {
        if (data instanceof JGraph) {
            super.setData(data);
            JGraph graph = (JGraph) data;
            graph.addMouseListener(mouseAdapter);
        } else {
            throw new IllegalArgumentException("Operation Files can only contain JGraph objects");
        }
    }

    /** @return boolean - true if this is modified, false otherwise */
    public boolean isModified() {
        return modified;
    }

    /**
     *
     */
    private void init() {
        name = translator.get(LanguageConstants.UNTITLED);
        setModified(true);
    }

    private class MyMouseAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent event) {
            super.mouseClicked(event);
            setModified(true);
        }
    }
}
