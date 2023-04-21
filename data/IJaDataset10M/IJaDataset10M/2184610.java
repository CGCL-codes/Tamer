package com.google.code.p.keytooliui.ktl.swing.splitpane;

import java.awt.Frame;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import com.google.code.p.keytooliui.ktl.swing.panel.PCntsMainAbs;
import com.google.code.p.keytooliui.ktl.swing.panel.PCntsMainLeft;
import com.google.code.p.keytooliui.ktl.swing.panel.PCntsMainRight;
import com.google.code.p.keytooliui.shared.lang.MySystem;

public class SPUIMainKtl extends JSplitPane implements TreeSelectionListener {

    public void setContextualHelpID() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setContextualHelpID();
    }

    public void setSelectedTaskCreateKst() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCreateKst();
    }

    public void setSelectedTaskCreateKprV3CDsa() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCreateKprV3CDsa();
    }

    public void setSelectedTaskCreateKprV1CDsa() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCreateKprV1CDsa();
    }

    public void setSelectedTaskCreateKprV3CEc() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCreateKprV3CEc();
    }

    public void setSelectedTaskCreateKprV1CEc() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCreateKprV1CEc();
    }

    public void setSelectedTaskCreateKprV3CRsa() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCreateKprV3CRsa();
    }

    public void setSelectedTaskCreateKprV1CRsa() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCreateKprV1CRsa();
    }

    public void setSelectedTaskCreateShkAll() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCreateShkAll();
    }

    public void setSelectedTaskKprFromKprKst() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprFromKprKst();
    }

    public void setSelectedTaskKprAnyToCsr() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprAnyToCsr();
    }

    public void setSelectedTaskTcrToCrt() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskTcrToCrt();
    }

    public void setSelectedTaskKprToKpr() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprToKpr();
    }

    public void setSelectedTaskCrtToSig() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCrtToSig();
    }

    public void setSelectedTaskWelcomeKtl() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskWelcomeKtl();
    }

    public void setSelectedTaskCmsToSig() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskCmsToSig();
    }

    public void setSelectedTaskXmlToSig() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskXmlToSig();
    }

    public void setSelectedTaskKprToSig() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprToSig();
    }

    public void setSelectedTaskKprToSCms() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprToSCms();
    }

    public void setSelectedTaskKprToXmlSig() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprToXmlSig();
    }

    public void setSelectedTaskKprToJarSig() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprToJarSig();
    }

    public void setSelectedTaskDirToArc() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskDirToArc();
    }

    public void setSelectedTaskJarToSig() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskJarToSig();
    }

    public void setSelectedTaskShkToCryptEnc() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskShkToCryptEnc();
    }

    public void setSelectedTaskTcrRsaToCryptEnc() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskTcrRsaToCryptEnc();
    }

    public void setSelectedTaskKprRsaToCryptEnc() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprRsaToCryptEnc();
    }

    public void setSelectedTaskIOShkOut() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskIOShkOut();
    }

    public void setSelectedTaskIOShkIn() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskIOShkIn();
    }

    public void setSelectedTaskShkToCryptDec() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskShkToCryptDec();
    }

    public void setSelectedTaskKprRsaToCryptDec() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprRsaToCryptDec();
    }

    public void setSelectedTaskKprToCrt() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprToCrt();
    }

    public void setSelectedTaskTcrFromCCa() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskTcrFromCCa();
    }

    public void setSelectedTaskTcrFromCrt() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskTcrFromCrt();
    }

    public void setSelectedTaskKprFromKprDer() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprFromKprDer();
    }

    public void setSelectedTaskKprFromKprPem() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprFromKprPem();
    }

    public void setSelectedTaskKprAnyFromCrt() {
        if (this._pnlRight != null) ((PCntsMainRight) this._pnlRight).setSelectedTaskKprAnyFromCrt();
    }

    public SPUIMainKtl(Frame frmParent) {
        this._pnlLeft = new PCntsMainLeft(frmParent, (TreeSelectionListener) this);
        this._pnlRight = new PCntsMainRight(frmParent);
    }

    public boolean init() {
        if (!this._pnlLeft.init()) return false;
        if (!this._pnlRight.init()) return false;
        super.setOneTouchExpandable(true);
        super.setDividerLocation(250);
        super.setLeftComponent(this._pnlLeft);
        super.setRightComponent(this._pnlRight);
        return true;
    }

    public void destroy() {
        if (this._pnlLeft != null) {
            this._pnlLeft.destroy();
            this._pnlLeft = null;
        }
        if (this._pnlRight != null) {
            this._pnlRight.destroy();
            this._pnlRight = null;
        }
    }

    public void valueChanged(TreeSelectionEvent evt) {
        String strMethod = "valueChanged(evt)";
        JTree tre = ((PCntsMainLeft) this._pnlLeft).getTree();
        DefaultMutableTreeNode mtn = (DefaultMutableTreeNode) tre.getLastSelectedPathComponent();
        if (mtn == null) return;
        if (!mtn.isLeaf()) return;
        Object obj = mtn.getUserObject();
        String str = (String) obj;
        if (str.compareTo(PCntsMainLeft.STR_NODE_WELCOME) == 0) {
            this.setSelectedTaskWelcomeKtl();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CRKST) == 0) {
            this.setSelectedTaskCreateKst();
            return;
        }
        DefaultMutableTreeNode mtnParent = (DefaultMutableTreeNode) mtn.getParent();
        String strParent = (String) mtnParent.getUserObject();
        if (str.compareTo(PCntsMainLeft.STR_NODE_CRENTPKDSA) == 0) {
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_KW_PK_V1C) == 0) this.setSelectedTaskCreateKprV1CDsa(); else if (strParent.compareTo(PCntsMainLeft.STR_NODE_KW_PK_V3C) == 0) this.setSelectedTaskCreateKprV3CDsa(); else {
                MySystem.s_printOutFlagDev(this, strMethod, "uncaught strParent:" + strParent);
                MySystem.s_printOutExit(this, strMethod, "failed to get either " + PCntsMainLeft.STR_NODE_KW_PK_V1C + ", or" + PCntsMainLeft.STR_NODE_KW_PK_V3C);
                return;
            }
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CRENTPKRSA) == 0) {
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_KW_PK_V1C) == 0) this.setSelectedTaskCreateKprV1CRsa(); else if (strParent.compareTo(PCntsMainLeft.STR_NODE_KW_PK_V3C) == 0) this.setSelectedTaskCreateKprV3CRsa(); else {
                MySystem.s_printOutFlagDev(this, strMethod, "uncaught strParent:" + strParent);
                MySystem.s_printOutExit(this, strMethod, "failed to get either " + PCntsMainLeft.STR_NODE_KW_PK_V1C + ", or" + PCntsMainLeft.STR_NODE_KW_PK_V3C);
                return;
            }
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CRENTPKEC) == 0) {
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_KW_PK_V1C) == 0) this.setSelectedTaskCreateKprV1CEc(); else if (strParent.compareTo(PCntsMainLeft.STR_NODE_KW_PK_V3C) == 0) this.setSelectedTaskCreateKprV3CEc(); else {
                MySystem.s_printOutFlagDev(this, strMethod, "uncaught strParent:" + strParent);
                MySystem.s_printOutExit(this, strMethod, "failed to get either " + PCntsMainLeft.STR_NODE_KW_PK_V1C + ", or" + PCntsMainLeft.STR_NODE_KW_PK_V3C);
                return;
            }
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_KW_SK) == 0) {
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_KW_ENTRY) != 0) {
                MySystem.s_printOutFlagDev(this, strMethod, "uncaught strParent:" + strParent);
                MySystem.s_printOutExit(this, strMethod, "failed to get " + PCntsMainLeft.STR_NODE_KW_ENTRY);
                return;
            }
            mtnParent = (DefaultMutableTreeNode) mtnParent.getParent();
            strParent = (String) mtnParent.getUserObject();
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_CREATE) == 0) this.setSelectedTaskCreateShkAll(); else if (strParent.compareTo(PCntsMainLeft.STR_NODE_IOIN) == 0) this.setSelectedTaskIOShkIn(); else if (strParent.compareTo(PCntsMainLeft.STR_NODE_IOOUT) == 0) this.setSelectedTaskIOShkOut(); else {
                MySystem.s_printOutFlagDev(this, strMethod, "uncaught strParent:" + strParent);
                MySystem.s_printOutExit(this, strMethod, "failed to get right parent of parent");
                return;
            }
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_KW_PKDER) == 0) {
            this.setSelectedTaskKprFromKprDer();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_KW_PKPEM) == 0) {
            this.setSelectedTaskKprFromKprPem();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_KW_PK) == 0) {
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_KW_ENTRY) != 0) {
                MySystem.s_printOutFlagDev(this, strMethod, "uncaught strParent:" + strParent);
                MySystem.s_printOutExit(this, strMethod, "failed to get " + PCntsMainLeft.STR_NODE_KW_ENTRY);
                return;
            }
            mtnParent = (DefaultMutableTreeNode) mtnParent.getParent();
            strParent = (String) mtnParent.getUserObject();
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_IOOUT) == 0) this.setSelectedTaskKprToKpr(); else {
                MySystem.s_printOutFlagDev(this, strMethod, "uncaught strParent:" + strParent);
                MySystem.s_printOutExit(this, strMethod, "failed to get right parent of parent");
                return;
            }
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_IOPKZCRT2CRT) == 0) {
            this.setSelectedTaskKprToCrt();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_IOPKZCRT2CSR) == 0) {
            this.setSelectedTaskKprAnyToCsr();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_IOCACRTREPLY) == 0) {
            this.setSelectedTaskKprAnyFromCrt();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_IOENTPKOTHER) == 0) {
            this.setSelectedTaskKprFromKprKst();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_KW_TCREGULAR) == 0) {
            this.setSelectedTaskTcrFromCrt();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_KW_TCROOTCA) == 0) {
            this.setSelectedTaskTcrFromCCa();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_KW_TC) == 0) {
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_KW_ENTRY) != 0) {
                MySystem.s_printOutFlagDev(this, strMethod, "uncaught strParent:" + strParent);
                MySystem.s_printOutExit(this, strMethod, "failed to get " + PCntsMainLeft.STR_NODE_KW_ENTRY);
                return;
            }
            mtnParent = (DefaultMutableTreeNode) mtnParent.getParent();
            strParent = (String) mtnParent.getUserObject();
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_IOOUT) == 0) this.setSelectedTaskTcrToCrt(); else {
                MySystem.s_printOutFlagDev(this, strMethod, "uncaught strParent:" + strParent);
                MySystem.s_printOutExit(this, strMethod, "failed to get right parent of parent");
                return;
            }
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CIPHERDETOTHER) == 0) {
            mtnParent = (DefaultMutableTreeNode) mtnParent.getParent();
            strParent = (String) mtnParent.getUserObject();
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_CIPHERSIGN) == 0) this.setSelectedTaskKprToSig(); else this.setSelectedTaskCrtToSig();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CIPHERDETCMS) == 0) {
            mtnParent = (DefaultMutableTreeNode) mtnParent.getParent();
            strParent = (String) mtnParent.getUserObject();
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_CIPHERSIGN) == 0) this.setSelectedTaskKprToSCms(); else this.setSelectedTaskCmsToSig();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CIPHEREMBXML) == 0) {
            mtnParent = (DefaultMutableTreeNode) mtnParent.getParent();
            strParent = (String) mtnParent.getUserObject();
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_CIPHERSIGN) == 0) this.setSelectedTaskKprToXmlSig(); else this.setSelectedTaskXmlToSig();
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CIPHEREMBJAR) == 0) {
            mtnParent = (DefaultMutableTreeNode) mtnParent.getParent();
            strParent = (String) mtnParent.getUserObject();
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_CIPHERSIGN) == 0) this.setSelectedTaskKprToJarSig(); else this.setSelectedTaskJarToSig();
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_ARCDIR) == 0) {
            this.setSelectedTaskDirToArc();
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CRYPTSK) == 0) {
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_CRYPTENC) == 0) this.setSelectedTaskShkToCryptEnc(); else this.setSelectedTaskShkToCryptDec();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CRYPTTC) == 0) {
            this.setSelectedTaskTcrRsaToCryptEnc();
            return;
        }
        if (str.compareTo(PCntsMainLeft.STR_NODE_CRYPTPK) == 0) {
            if (strParent.compareTo(PCntsMainLeft.STR_NODE_CRYPTENC) == 0) this.setSelectedTaskKprRsaToCryptEnc(); else this.setSelectedTaskKprRsaToCryptDec();
            return;
        }
    }

    PCntsMainAbs _pnlLeft = null;

    PCntsMainAbs _pnlRight = null;
}
