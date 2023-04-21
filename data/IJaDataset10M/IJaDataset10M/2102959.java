package org.gamio.conf;

import java.util.ArrayList;
import java.util.List;
import org.gamio.conf.routine.RouteTable;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 23 $ $Date: 2008-10-05 21:00:52 -0400 (Sun, 05 Oct 2008) $
 */
public final class Configuration {

    private InstProps instProps = null;

    private ChannelManagerProps channelManagerProps = null;

    private ServerManagerProps serverManagerProps = null;

    private ProcessorManagerProps processorManagerProps = null;

    private ClientManagerProps clientManagerProps = null;

    private MsgQueueProps msgQueueProps = null;

    private BufferPoolProps bufferPoolProps = null;

    private ThreadPoolProps threadPoolProps = null;

    private List<MsgletProps> msgletPropsList = new ArrayList<MsgletProps>();

    private List<ServerProps> serverPropsList = new ArrayList<ServerProps>();

    private List<ProcessorProps> processorPropsList = new ArrayList<ProcessorProps>();

    private List<ClientProps> clientPropsList = new ArrayList<ClientProps>();

    private List<PackerProps> packerPropsList = new ArrayList<PackerProps>();

    private List<DescriptorProps> descriptorPropsList = new ArrayList<DescriptorProps>();

    private MessageTable messageTable = new MessageTable();

    private RouteTable routeTable = new RouteTable();

    public ChannelManagerProps getChannelManagerProps() {
        return channelManagerProps;
    }

    public void setChannelManagerProps(ChannelManagerProps channelManagerProps) {
        this.channelManagerProps = channelManagerProps;
    }

    public ServerManagerProps getServerManagerProps() {
        return serverManagerProps;
    }

    public void setServerManagerProps(ServerManagerProps serverManagerProps) {
        this.serverManagerProps = serverManagerProps;
    }

    public ProcessorManagerProps getProcessorManagerProps() {
        return processorManagerProps;
    }

    public void setProcessorManagerProps(ProcessorManagerProps processorManagerProps) {
        this.processorManagerProps = processorManagerProps;
    }

    public ClientManagerProps getClientManagerProps() {
        return clientManagerProps;
    }

    public void setClientManagerProps(ClientManagerProps clientManagerProps) {
        this.clientManagerProps = clientManagerProps;
    }

    public BufferPoolProps getBufferPoolProps() {
        return bufferPoolProps;
    }

    public void setBufferPoolProps(BufferPoolProps bufferPoolProps) {
        this.bufferPoolProps = bufferPoolProps;
    }

    public MsgQueueProps getMsgQueueProps() {
        return msgQueueProps;
    }

    public void setMsgQueueProps(MsgQueueProps msgQueueProps) {
        this.msgQueueProps = msgQueueProps;
    }

    public InstProps getInstProps() {
        return instProps;
    }

    public void setInstProps(InstProps instProps) {
        this.instProps = instProps;
    }

    public ThreadPoolProps getThreadPoolProps() {
        return threadPoolProps;
    }

    public void setThreadPoolProps(ThreadPoolProps threadPoolProps) {
        this.threadPoolProps = threadPoolProps;
    }

    public void addMsgletProps(MsgletProps msgletProps) {
        msgletPropsList.add(msgletProps);
    }

    public List<MsgletProps> getMsgletPropsList() {
        return msgletPropsList;
    }

    public void addServerProps(ServerProps serverProps) {
        serverPropsList.add(serverProps);
    }

    public List<ServerProps> getServerPropsList() {
        return serverPropsList;
    }

    public void addProcessorProps(ProcessorProps processorProps) {
        processorPropsList.add(processorProps);
    }

    public List<ProcessorProps> getProcessorPropsList() {
        return processorPropsList;
    }

    public void addClientProps(ClientProps clientProps) {
        clientPropsList.add(clientProps);
    }

    public List<ClientProps> getClientPropsList() {
        return clientPropsList;
    }

    public void addPackerProps(PackerProps packerProps) {
        packerPropsList.add(packerProps);
    }

    public List<PackerProps> getPackerPropsList() {
        return packerPropsList;
    }

    public void addDescriptorProps(DescriptorProps descriptorProps) {
        descriptorPropsList.add(descriptorProps);
    }

    public List<DescriptorProps> getDescriptorPropsList() {
        return descriptorPropsList;
    }

    public void setMessageTable(MessageTable messageTable) {
        this.messageTable = messageTable;
    }

    public MessageTable getMessageTable() {
        return messageTable;
    }

    public RouteTable getRouteTable() {
        return routeTable;
    }

    public void setRouteTable(RouteTable routeTable) {
        this.routeTable = routeTable;
    }
}
