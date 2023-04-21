package com.patientis.framework.controls.custom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.JPanel;
import com.patientis.business.common.ICustomController;
import com.patientis.client.action.BaseAction;
import com.patientis.framework.controls.ControlInteraction;
import com.patientis.framework.controls.IComponent;
import com.patientis.framework.controls.IControl;
import com.patientis.framework.controls.IControlInteraction;
import com.patientis.framework.controls.IDisplayFile;
import com.patientis.framework.controls.ISMemory;
import com.patientis.framework.controls.listeners.ISActionListener;
import com.patientis.framework.locale.ImageUtil;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.IMediateMessages;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.model.common.Converter;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.reference.ActionReference;
import com.smardec.mousegestures.MouseGestures;
import com.smardec.mousegestures.MouseGesturesListener;

/**
 * One line class description
 *
 */
public class ISGesturePanel extends JPanel implements IComponent, IDisplayFile {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private ISGesturePanel self = this;

    /**
	 * 
	 */
    private boolean activeGesture = false;

    /**
	 * 
	 */
    private BufferedImage bufferImage = null;

    /**
	 * Context and actions
	 */
    private IControlInteraction interaction = new ControlInteraction();

    /**
	 * Weak reference to the model listener
	 */
    private ISActionListener weakModelListener = null;

    /**
	 * Value
	 */
    private long valueFileId = 0;

    public enum Gesture {

        LEFT, RIGHT, UP, DOWN, LEFTRIGHT, UNKOWN
    }

    ;

    /** 
	 * Default constructor
	 */
    public ISGesturePanel() {
        super(null);
        initialize();
    }

    /**
	 * 
	 * @param gesture
	 * @return
	 */
    public static Gesture getGesture(String gesture) {
        if (Converter.isTrimmedSameIgnoreCase(gesture, "R")) {
            return Gesture.RIGHT;
        } else if (Converter.isTrimmedSameIgnoreCase(gesture, "L")) {
            return Gesture.LEFT;
        } else if (Converter.isTrimmedSameIgnoreCase(gesture, "U")) {
            return Gesture.UP;
        } else if (Converter.isTrimmedSameIgnoreCase(gesture, "D")) {
            return Gesture.DOWN;
        } else if (Converter.isTrimmedSameIgnoreCase(gesture, "LR")) {
            return Gesture.LEFTRIGHT;
        } else {
            return Gesture.UNKOWN;
        }
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#addActionListener(java.awt.event.ActionListener)
	 */
    public void addActionListener(ActionListener l) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#addNotifyModelListener(com.patientis.framework.controls.listeners.ISActionListener)
	 */
    public void addNotifyModelListener(final ISActionListener listener) {
        weakModelListener = listener;
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#getValue()
	 */
    public Object getValue() throws Exception {
        return valueFileId;
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#isIconEnabled()
	 */
    public boolean isIconEnabled() {
        return false;
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#release()
	 */
    public void release() {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setDisplayZero(boolean)
	 */
    public void setDisplayZero(boolean displayZero) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setEditable(boolean)
	 */
    public void setEditable(boolean editable) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setFormat(java.lang.String)
	 */
    public void setFormat(String format) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setIcon(javax.swing.Icon)
	 */
    public void setIcon(Icon icon) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setMediator(com.patientis.framework.scripting.IMediateMessages, com.patientis.framework.controls.IControlInteraction)
	 */
    public void setMediator(IMediateMessages mediator, IControlInteraction control) {
        interaction.setContextRefId(control.getContextRefId());
        interaction.setActionViewRefId(control.getActionViewRefId());
        interaction.setDefaultActionScript(control.getDefaultActionScript());
        if (control.getDefaultActionRefId() > 0 || control.getRightClickActionRefId() > 0) {
            interaction.setDefaultActionRefId(control.getDefaultActionRefId());
            interaction.setRightClickActionRefId(control.getRightClickActionRefId());
        }
        if (control.getSelectActionRefId() > 0) {
            interaction.setSelectActionRefId(control.getSelectActionRefId());
            addSelectAction(mediator);
        }
        if (control.getRightArrowActionRefId() > 0) {
            interaction.setRightArrowActionRefId(control.getRightArrowActionRefId());
        }
        setValueAction(mediator, control.getContextRefId(), control.getDefaultActionRefId());
        if (control.isDragSelectedModel()) {
            addDragHandler();
        }
        addMouseGestures(mediator, control);
    }

    /**
	 * Value should be the file id
	 * 
	 * @see com.patientis.framework.controls.IComponent#setValue(java.lang.Object)
	 */
    public void setValue(Object value) throws Exception {
        if (value != null) {
            long fileId = Converter.convertLong(value);
            if (fileId > 0 && fileId != valueFileId) {
                valueFileId = fileId;
                bufferImage = ImageUtil.getImageForFileId(fileId, true);
                setPreferredSize(new Dimension(bufferImage.getWidth(), bufferImage.getHeight()));
                repaint();
                if (weakModelListener != null) {
                    weakModelListener.actionExecuted(new ActionEvent(self, 0, null));
                }
            } else if (fileId == 0) {
                bufferImage = null;
                valueFileId = 0;
                repaint();
            }
        }
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setValueAction(com.patientis.framework.scripting.IMediateMessages, int, int)
	 */
    public void setValueAction(IMediateMessages mediator, int context, int defaultActionRefId) {
    }

    /**
	 * 
	 */
    public void initialize() {
        ISMemory.getInstance().add(this);
        setBackground(Color.white);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                activeGesture = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                activeGesture = false;
            }
        });
    }

    /**
	 * @see java.lang.Object#finalize()
	 */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        ISMemory.getInstance().remove(this);
    }

    /**
	 * Add a listenering on click
	 * 
	 * @param waitCursor
	 * @param column
	 */
    public void addSelectAction(final IMediateMessages mediator) {
    }

    /**
	 * 
	 */
    private void addDragHandler() {
    }

    private void addMouseGestures(final IMediateMessages mediator, final IControlInteraction control) {
        MouseGestures mouseGestures = new MouseGestures();
        mouseGestures.setMouseButton(MouseEvent.BUTTON1_MASK);
        mouseGestures.addMouseGesturesListener(new MouseGesturesListener() {

            public void gestureMovementRecognized(String currentGesture) {
                if (!activeGesture) {
                    return;
                }
            }

            public void processGesture(String gesture) {
                if (!activeGesture) {
                    return;
                }
                try {
                    BaseAction action = new BaseAction(ActionReference.SYSTEMEXECUTETRANSITION);
                    action.setContextRefId(control.getContextRefId());
                    action.setValue(gesture);
                    mediator.receive(ISEvent.EXECUTEACTION, action);
                } catch (Exception ex) {
                    Log.exception(ex);
                }
            }
        });
        mouseGestures.start();
    }

    /**
	 * @return the bufferImage
	 */
    public BufferedImage getBufferImage() {
        return bufferImage;
    }

    /**
	 * @param bufferImage the bufferImage to set
	 */
    public void setBufferImage(BufferedImage bufferImage) {
        this.bufferImage = bufferImage;
    }

    @Override
    public void setFile(long fileId) throws Exception {
        setValue(fileId);
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setControlProperties(com.patientis.framework.controls.IControl)
	 */
    @Override
    public void setControlProperties(IControl control, IBaseModel mainModel, IBaseModel controlModel, IMediateMessages mediator) {
    }

    /**
	 * 
	 * @see com.patientis.framework.controls.IComponent#updateModelListener()
	 */
    @Override
    public void updateModelListener() throws Exception {
    }

    /**
	 * 
	 * @see com.patientis.framework.controls.IComponent#isEditable()
	 */
    @Override
    public boolean isEditable() {
        return false;
    }

    /**
	 * 
	 * @see com.patientis.framework.controls.IComponent#savingForm()
	 */
    @Override
    public void savingForm() throws Exception {
    }

    /**
	 * 
	 * @see com.patientis.framework.controls.IComponent#getCustomController()
	 */
    @Override
    public ICustomController getCustomController() {
        return null;
    }

    /**
	 * 
	 * @see com.patientis.framework.controls.IComponent#setCustomController(com.patientis.business.common.ICustomController)
	 */
    @Override
    public void setCustomController(ICustomController controller) {
    }
}
