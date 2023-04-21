package j4nn.editor.ports;

import j4nn.network.LayerInterface;
import org.netbeans.graph.api.model.builtin.GraphPort;

public class LayerInterfacePort extends GraphPort {

    private LayerInterface layerInterface;

    public LayerInterfacePort(LayerInterface layerInterface) {
        this.layerInterface = layerInterface;
    }

    public LayerInterface getLayerInterface() {
        return layerInterface;
    }

    public void setLayerInterface(LayerInterface layerInterface) {
        this.layerInterface = layerInterface;
    }

    public int getLayerInterfaceSize() {
        return this.layerInterface.getSize();
    }

    public void setLayerInterfaceSize(int size) {
        this.layerInterface.setSize(size);
    }
}
