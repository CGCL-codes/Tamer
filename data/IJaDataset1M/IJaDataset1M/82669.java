package org.fudaa.fudaa.commun.undo;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import com.memoire.bu.BuCommonImplementation;
import org.fudaa.ctulu.CtuluCmdMngListener;
import org.fudaa.ctulu.CtuluCommandManager;
import org.fudaa.ctulu.CtuluUndoRedoInterface;
import org.fudaa.fudaa.commun.impl.Fudaa;
import org.fudaa.fudaa.ressource.FudaaResource;

/**
 * Class for undoredo manager with concurrent access. Each internal frame implementing {@link CtuluUndoRedoInterface}
 * has its own manager, independant from the others.
 *
 * @author deniger
 * @version $Id: FudaaUndoCmdMngListener.java 6192 2011-03-30 12:56:27Z bmarchan $
 */
public class FudaaUndoCmdMngListener implements CtuluCmdMngListener {

    BuCommonImplementation impl_;

    CtuluCommandManager cmdActif_;

    /**
   * @param _impl l'impl parent
   */
    public FudaaUndoCmdMngListener(final BuCommonImplementation _impl) {
        impl_ = _impl;
    }

    /**
   * @see javax.swing.event.InternalFrameListener#internalFrameActivated(javax.swing.event.InternalFrameEvent)
   */
    public void internalFrameActivated(final InternalFrameEvent _e) {
        if (_e.getInternalFrame() instanceof CtuluUndoRedoInterface) {
            final CtuluUndoRedoInterface c = (CtuluUndoRedoInterface) _e.getInternalFrame();
            if (c.getCmdMng() == null) {
                return;
            }
            if (c.getCmdMng().getListener() != this) {
                c.getCmdMng().setListener(this);
            }
            setActive(c, true);
        }
    }

    /**
   * @see javax.swing.event.InternalFrameListener#internalFrameClosed(javax.swing.event.InternalFrameEvent)
   */
    public void internalFrameClosed(final InternalFrameEvent _e) {
    }

    /**
   * @see javax.swing.event.InternalFrameListener#internalFrameClosing(javax.swing.event.InternalFrameEvent)
   */
    public void internalFrameClosing(final InternalFrameEvent _e) {
    }

    /**
   * @see javax.swing.event.InternalFrameListener#internalFrameDeactivated(javax.swing.event.InternalFrameEvent)
   */
    public void internalFrameDeactivated(final InternalFrameEvent _evt) {
        if (_evt.getInternalFrame() instanceof CtuluUndoRedoInterface) {
            final CtuluUndoRedoInterface c = (CtuluUndoRedoInterface) _evt.getInternalFrame();
            setActive(c, false);
        } else {
            setActive(null, false);
        }
    }

    /**
   * @see javax.swing.event.InternalFrameListener#internalFrameDeiconified(javax.swing.event.InternalFrameEvent)
   */
    public void internalFrameDeiconified(final InternalFrameEvent _e) {
    }

    /**
   * @see javax.swing.event.InternalFrameListener#internalFrameIconified(javax.swing.event.InternalFrameEvent)
   */
    public void internalFrameIconified(final InternalFrameEvent _e) {
    }

    /**
   * @see javax.swing.event.InternalFrameListener#internalFrameOpened(javax.swing.event.InternalFrameEvent)
   */
    public void internalFrameOpened(final InternalFrameEvent _e) {
    }

    /**
   * @param _m la manager a activer
   * @param _b le nouvel etat
   */
    public void setActive(final CtuluUndoRedoInterface _m, final boolean _b) {
        String lbUndo = FudaaResource.FUDAA.getString("D�faire");
        String lbRedo = FudaaResource.FUDAA.getString("Refaire");
        if (_b && (_m != null) && (_m.getCmdMng() != null)) {
            String name;
            name = _m.getCmdMng().getUndoName();
            lbUndo += (name == null ? "" : " \"" + name + "\"");
            name = _m.getCmdMng().getRedoName();
            lbRedo += (name == null ? "" : " \"" + name + "\"");
            impl_.setEnabledForAction("DEFAIRE", _m.getCmdMng().canUndo());
            impl_.setEnabledForAction("REFAIRE", _m.getCmdMng().canRedo());
        } else {
            impl_.setEnabledForAction("DEFAIRE", false);
            impl_.setEnabledForAction("REFAIRE", false);
        }
        impl_.setDynamicTextForAction("DEFAIRE", lbUndo);
        impl_.setDynamicTextForAction("REFAIRE", lbRedo);
        if (_m != null) {
            _m.setActive(_b);
        }
        if (_b && (_m != null)) {
            cmdActif_ = _m.getCmdMng();
        } else {
            cmdActif_ = null;
        }
    }

    public void undoredoStateChange(final CtuluCommandManager _source) {
        if (_source != cmdActif_) {
            return;
        }
        String lbUndo = FudaaResource.FUDAA.getString("D�faire");
        String lbRedo = FudaaResource.FUDAA.getString("Refaire");
        String name;
        name = _source.getUndoName();
        lbUndo += (name == null ? "" : " \"" + name + "\"");
        name = _source.getRedoName();
        lbRedo += (name == null ? "" : " \"" + name + "\"");
        impl_.setEnabledForAction("DEFAIRE", _source.canUndo());
        impl_.setEnabledForAction("REFAIRE", _source.canRedo());
        impl_.setDynamicTextForAction("DEFAIRE", lbUndo);
        impl_.setDynamicTextForAction("REFAIRE", lbRedo);
        final JInternalFrame[] fs = impl_.getAllInternalFrames();
        if (fs != null) {
            for (int i = fs.length - 1; i >= 0; i--) {
                if (fs[i] instanceof CtuluUndoRedoInterface) {
                    final CtuluUndoRedoInterface c = (CtuluUndoRedoInterface) fs[i];
                    c.clearCmd(_source);
                }
            }
        }
    }
}
