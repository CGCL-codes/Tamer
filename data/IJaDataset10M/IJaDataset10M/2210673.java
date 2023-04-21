package br.ufal.tci.nexos.arcolive.beans;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * @author <a href="mailto:felipe@labpesquisas.tci.ufal.br">Felipe Barros Pontes</a>.
 * @author <a href="mailto:leandro@labpesquisas.tci.ufal.br">Leandro Melo de
 *         Sales</a>.
 */
public class PlayerInternalFrameBeanInfo extends SimpleBeanInfo {

    private static final Class arcolivePlayerInternalFrameClass = PlayerInternalFrame.class;

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor transmissionPort = new PropertyDescriptor("transmissionPort", arcolivePlayerInternalFrameClass);
            transmissionPort.setDisplayName("Transmission Port");
            transmissionPort.setShortDescription("The transmission port.");
            PropertyDescriptor receptionPort = new PropertyDescriptor("receptionPort", arcolivePlayerInternalFrameClass);
            receptionPort.setDisplayName("Reception Port");
            receptionPort.setShortDescription("The reception port.");
            PropertyDescriptor title = new PropertyDescriptor("title", arcolivePlayerInternalFrameClass);
            title.setDisplayName("Title");
            title.setShortDescription("The internal player title.");
            PropertyDescriptor receptorHost = new PropertyDescriptor("receptorHost", arcolivePlayerInternalFrameClass);
            receptorHost.setDisplayName("Receptor host");
            receptorHost.setShortDescription("The receptor host.");
            PropertyDescriptor senderHost = new PropertyDescriptor("senderHost", arcolivePlayerInternalFrameClass);
            senderHost.setDisplayName("Sender host");
            senderHost.setShortDescription("The sender host.");
            PropertyDescriptor bufferLength = new PropertyDescriptor("bufferLength", arcolivePlayerInternalFrameClass);
            bufferLength.setDisplayName("Buffer length");
            bufferLength.setShortDescription("The buffer length.");
            PropertyDescriptor playerType = new PropertyDescriptor("playerType", arcolivePlayerInternalFrameClass);
            playerType.setDisplayName("Player Type");
            playerType.setShortDescription("The player type.");
            playerType.setPropertyEditorClass(PlayerTypePropertyEditor.class);
            PropertyDescriptor[] propertyDescriptors = { transmissionPort, receptionPort, title, receptorHost, senderHost, bufferLength, playerType };
            return propertyDescriptors;
        } catch (IntrospectionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(PlayerInternalFrame.class);
    }

    public int getDefaultPropertyIndex() {
        return 0;
    }

    public Image getIcon(int type) {
        String name = "";
        if (type == BeanInfo.ICON_COLOR_16x16) {
            name = "COLOR_16x16";
        } else if (type == BeanInfo.ICON_COLOR_32x32) {
            name = "COLOR_32x32";
        } else if (type == BeanInfo.ICON_MONO_16x16) {
            name = "MONO_16x16";
        } else if (type == BeanInfo.ICON_MONO_32x32) {
            name = "MONO_32x32";
        }
        return loadImage("ArCoLIVEPlayerInternalFrame_" + name + ".jpg");
    }
}
