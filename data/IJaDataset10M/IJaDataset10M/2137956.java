package i18ntool.action;

import i18ntool.dialogs.SearchItemByValueDialog;
import i18ntool.entity.TreeNode;
import i18ntool.entity.ValueEntity;
import i18ntool.property.Resource;
import i18ntool.util.NodeAssistant;
import i18ntool.util.ViewAssistant;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;
import iceworld.fernado.search.PatternConstructor;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class SearchItemByValueAction extends Action implements IWorkbenchAction {

    public static final String ID = SearchItemByValueAction.class.getName();

    private static final Logger log = Logger.getLogger(ID);

    private final IWorkbenchWindow window;

    public SearchItemByValueAction(final IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("&" + Resource.SEARCH_ITEMS_BY_VALUE);
        setToolTipText(Resource.SEARCH_ITEMS_BY_VALUE);
    }

    @Override
    public void dispose() {
        log.log(Level.INFO, ID + ".dispose()");
    }

    @Override
    public void run() {
        doAction();
    }

    public void doAction() {
        log.log(Level.INFO, ID + ".doAction() start");
        SearchItemByValueDialog sibvd = new SearchItemByValueDialog(window.getShell());
        int code = sibvd.open();
        if (Window.OK == code) {
            NodeAssistant.getInstance().setTreeNode(findNodes(sibvd.getResults()));
            IViewPart searchView = ViewAssistant.getInstance().findView(i18ntool.view.SearchView.ID);
            if (null == searchView) {
                ViewAssistant.getInstance().showView(i18ntool.view.SearchView.ID);
            } else {
                ((i18ntool.view.SearchView) searchView).refresh();
            }
        }
        sibvd.close();
        log.log(Level.INFO, ID + ".doAction() end");
    }

    private INode findNodes(final String[] keys) {
        INode result = NodeAssistant.getInstance().getSearchTreeNode();
        NodeAssistant.getInstance().clear(result);
        for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
            if (Type.LEAF == node.getType()) {
                boolean exist = false;
                Pattern p = PatternConstructor.createPattern(keys[0], Boolean.getBoolean(keys[1]), Boolean.getBoolean(keys[2]));
                for (Map.Entry<String, ValueEntity> me : ((TreeNode) node).getValueMap().entrySet()) {
                    Matcher m = p.matcher(me.getValue().getCurrent());
                    if (m.find()) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
                    result.addChild(node);
                    exist = false;
                }
            }
        }
        return result;
    }
}
