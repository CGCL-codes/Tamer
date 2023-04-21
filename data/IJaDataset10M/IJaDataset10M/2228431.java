package orcajo.azada.table.ktable;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.olap4j.OlapException;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Member;
import orcajo.azada.core.model.ModelException;
import orcajo.azada.core.model.QueryManager;
import orcajo.azada.core.transferable.MetadataElementInfo;
import orcajo.azada.core.transferable.MetadataElementInfoManager;
import orcajo.azada.core.transferable.MetadataElementInfoTransfer;

/**
 * 
 * @author fsaz
 *
 */
public class DropSlicerAdapter extends DropTargetAdapter {

    private QueryManager qManager;

    int detailOnEnter = DND.DROP_NONE;

    public DropSlicerAdapter(QueryManager qManager) {
        super();
        this.qManager = qManager;
    }

    public void dragOver(DropTargetEvent event) {
        if (qManager.canNavigate() && canDrop(event)) {
            event.detail = detailOnEnter;
        } else {
            event.detail = DND.DROP_NONE;
        }
    }

    public void dragEnter(DropTargetEvent event) {
        detailOnEnter = event.detail;
    }

    public void dragLeave(DropTargetEvent event) {
        event.detail = detailOnEnter;
    }

    public void drop(DropTargetEvent event) {
        if (event.data == null || (!(event.data instanceof MetadataElementInfo[]))) {
            event.detail = DND.DROP_NONE;
            return;
        }
        MetadataElementInfo[] meis = (MetadataElementInfo[]) event.data;
        if (meis != null && meis.length > 0) {
            MetadataElementInfo mei = meis[0];
            if (MetadataElementInfoManager.valid(qManager, mei)) {
                Member dragedMember = null;
                Hierarchy dragedHierarchy = null;
                if (mei.type == MetadataElementInfo.T_DIMENSION) {
                    Dimension dim = MetadataElementInfoManager.getDimension(qManager, mei);
                    if (dim != null) {
                        dragedHierarchy = dim.getDefaultHierarchy();
                        ;
                    }
                } else if (mei.type == MetadataElementInfo.T_HIERARCHY) {
                    dragedHierarchy = MetadataElementInfoManager.getHierarchy(qManager, mei);
                } else if (mei.type == MetadataElementInfo.T_MEMBER) {
                    dragedMember = MetadataElementInfoManager.getMember(qManager, mei);
                }
                if (dragedMember != null && qManager.canAddToSlicer(dragedMember)) {
                    try {
                        qManager.addToSlicer(dragedMember);
                        event.detail = DND.DROP_MOVE;
                        return;
                    } catch (ModelException e) {
                    }
                }
                if (dragedHierarchy != null) {
                    try {
                        Member defaulMember = dragedHierarchy.getDefaultMember();
                        if (qManager.canAddToSlicer(defaulMember)) {
                            qManager.addToSlicer(defaulMember);
                            event.detail = DND.DROP_MOVE;
                            return;
                        }
                    } catch (ModelException e) {
                    } catch (OlapException e) {
                    }
                }
            }
        }
        event.detail = DND.DROP_NONE;
    }

    private boolean canDrop(DropTargetEvent event) {
        MetadataElementInfoTransfer transfer = MetadataElementInfoTransfer.getInstance();
        if (transfer.isSupportedType(event.currentDataType)) {
            MetadataElementInfo[] meis = null;
            meis = (MetadataElementInfo[]) transfer.nativeToJava(event.currentDataType);
            if (meis == null) {
                meis = MetadataElementInfoManager.LAST;
            }
            if (meis != null) {
                return canDropProbable(event, meis);
            } else {
                return canDropOptimistic(event);
            }
        }
        return false;
    }

    private boolean canDropOptimistic(DropTargetEvent event) {
        return true;
    }

    private boolean canDropProbable(DropTargetEvent event, MetadataElementInfo[] meis) {
        if (meis != null && meis.length > 0) {
            MetadataElementInfo mei = meis[0];
            if (MetadataElementInfoManager.valid(qManager, mei)) {
                Member dragedMember = null;
                Hierarchy dragedHier = null;
                if (mei.type == MetadataElementInfo.T_DIMENSION) {
                    Dimension dim = MetadataElementInfoManager.getDimension(qManager, mei);
                    if (dim != null) {
                        dragedHier = dim.getDefaultHierarchy();
                        ;
                    }
                } else if (mei.type == MetadataElementInfo.T_HIERARCHY) {
                    dragedHier = MetadataElementInfoManager.getHierarchy(qManager, mei);
                } else if (mei.type == MetadataElementInfo.T_MEMBER) {
                    dragedMember = MetadataElementInfoManager.getMember(qManager, mei);
                }
                if (dragedMember != null) {
                    return qManager.canAddToSlicer(dragedMember);
                }
                if (dragedHier != null) {
                    try {
                        return qManager.canAddToSlicer(dragedHier.getDefaultMember());
                    } catch (OlapException e) {
                    }
                }
            }
        }
        return false;
    }
}
