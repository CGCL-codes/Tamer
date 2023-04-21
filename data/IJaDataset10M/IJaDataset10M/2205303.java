package com.generatescape.dnd;

import java.util.HashSet;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import com.generatescape.newtreemodel.CanalGraph;
import com.generatescape.newtreemodel.NewCanalNode;
import com.generatescape.wizards.CreateMediaMinderWizardSearchCanalSelectionPage;

/*******************************************************************************
 * Copyright (c) 2005, 2007 GenerateScape and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the GNU General Public License which accompanies this distribution, and is
 * available at http://www.gnu.org/copyleft/gpl.html
 * 
 * @author kentgibson : http://www.bigblogzoo.com
 * 
 ******************************************************************************/
public class WizardSearchDropAdapter extends ViewerDropAdapter {

    static Logger log = Logger.getLogger(WizardSearchDropAdapter.class.getName());

    private CanalGraph cg;

    private TreeViewer viewer;

    private HashSet set;

    /**
   * @param viewer
   * @param cg
   * @param set
   */
    public WizardSearchDropAdapter(TreeViewer viewer, CanalGraph cg, HashSet set) {
        super(viewer);
        this.cg = cg;
        this.viewer = viewer;
        this.set = set;
    }

    /**
   * Method declared on ViewerDropAdapter
   */
    public boolean performDrop(Object data) {
        NewCanalNode target = (NewCanalNode) getCurrentTarget();
        if (target == null) {
            target = CreateMediaMinderWizardSearchCanalSelectionPage.top;
        }
        NewCanalNode[] toDrop = (NewCanalNode[]) data;
        for (int i = 0; i < toDrop.length; i++) {
            if (toDrop[i].equals(target)) {
                return false;
            }
            if (set.contains(toDrop[i].getKey())) {
                return false;
            }
        }
        for (int i = 0; i < toDrop.length; i++) {
            cg.addChild(target, toDrop[i]);
            set.add(toDrop[i].getKey());
            viewer.add(target, toDrop[i]);
            viewer.reveal(toDrop[i]);
        }
        viewer.refresh();
        CreateMediaMinderWizardSearchCanalSelectionPage.allowPageComplete();
        return true;
    }

    /**
   * Method declared on ViewerDropAdapter
   */
    public boolean validateDrop(Object target, int op, TransferData type) {
        log.info("Wiz Validating " + WizardTransfer.getInstance().isSupportedType(type));
        return WizardTransfer.getInstance().isSupportedType(type);
    }

    /**
   * @param cg
   *          The cg to set.
   */
    public void setCg(CanalGraph cg) {
        this.cg = cg;
    }
}
