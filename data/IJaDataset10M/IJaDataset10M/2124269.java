package org.xmlvm.ios;

import java.util.*;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public class UITableViewController extends UIViewController {

    /**
	 * - (id)initWithStyle:(UITableViewStyle)style;
	 */
    public UITableViewController(int style) {
    }

    /** Default constructor */
    UITableViewController() {
    }

    /**
	 * @property(nonatomic,retain) UITableView *tableView;
	 */
    public UITableView getTableView() {
        throw new RuntimeException("Stub");
    }

    /**
	 * @property(nonatomic,retain) UITableView *tableView;
	 */
    public void setTableView(UITableView tableView) {
        throw new RuntimeException("Stub");
    }

    /**
	 * @property(nonatomic) BOOL clearsSelectionOnViewWillAppear ;
	 */
    public boolean getClearsSelectionOnViewWillAppear() {
        throw new RuntimeException("Stub");
    }

    /**
	 * @property(nonatomic) BOOL clearsSelectionOnViewWillAppear ;
	 */
    public void setClearsSelectionOnViewWillAppear(boolean clearsSelectionOnViewWillAppear) {
        throw new RuntimeException("Stub");
    }
}
