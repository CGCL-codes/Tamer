package com.jw.mos.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.JFrame;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.jw.mos.configuration.IXmlProvideable;
import com.jw.mos.gui.FrameMain;
import com.jw.mos.gui.GuiUtil;
import com.jw.mos.gui.components.connector.ConnectionPoint;
import com.jw.mos.gui.components.connector.ConnectorManager;
import com.jw.mos.gui.components.connector.ProcessConnectionPointContainer;
import com.jw.mos.gui.components.resource.OwnResourceContainer;
import com.jw.mos.gui.components.resource.ResourceContainer;
import com.jw.mos.gui.components.resource.SharedResourceContainer;
import com.jw.mos.gui.components.support.IConnectable;
import com.jw.mos.gui.components.support.IConnectableSource;
import com.jw.mos.gui.components.support.IConnectableSourceAvailableDestination;
import com.jw.mos.gui.components.support.IDialogPropertyEditable;
import com.jw.mos.gui.components.support.IParentRemovable;
import com.jw.mos.gui.components.support.IPropertyEditable;
import com.jw.mos.gui.components.support.IRemovable;
import com.jw.mos.gui.components.support.mouse.PropertyEditorMouseSupport;
import com.jw.mos.gui.data.IDataProvidable;
import com.jw.mos.gui.data.ProcessData;
import com.jw.mos.system.IGuiConst;
import com.jw.mos.system.IXmlTags;
import com.jw.mos.system.exception.MoSGuiException;
import com.jw.mos.system.exception.MoSXmlConfigurationException;

/**
 * @author Administrator
 *
 */
public class Process extends ProcessSymbol implements IConnectableSource<Process>, Comparable<Process>, IRemovable, IConnectableSourceAvailableDestination<SharedResource>, IXmlProvideable, IPropertyEditable<Process> {

    private IParentRemovable<Process> parent;

    /**Component data*/
    private final ProcessData data;

    /**Gui elements*/
    private final ResourceContainer<OwnResource> ownResources = new OwnResourceContainer(this);

    private final ResourceContainer<SharedResource> sharedResources = new SharedResourceContainer(this);

    private final ProcessConnectionPointContainer connectorContainer = new ProcessConnectionPointContainer();

    public Process(IParentRemovable<Process> parent, int rank) {
        this.parent = parent;
        data = new ProcessData(rank);
        init();
    }

    private void init() {
        switchDnDSupport(false);
        setMaximumSize(null);
        setToolTipText("<HTML>Process<BR>rank: " + data.getRank() + "</HTML>");
        new ConnectorManager<Process>(this);
        addContainers();
        addPropertyEditorMouseSupport();
    }

    private void addContainers() {
        GuiUtil.setBounds(sharedResources, 45, 4, sharedResources.getDimension());
        add(sharedResources);
        GuiUtil.setBounds(connectorContainer, 0, 5, connectorContainer.getDimension());
        add(connectorContainer);
        connectorContainer.validate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        removeAll();
        addContainers();
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        g2.setFont(IGuiConst.WORK_PANEL_FONT);
        g2.setColor(Color.BLACK);
        g2.drawString("rank: " + String.valueOf(data.getRank()), 10, 15);
    }

    /********************************************  {@link IDialogPropertyEditable} **************************/
    @Override
    public void addPropertyEditorMouseSupport() {
        addMouseListener(new PropertyEditorMouseSupport(ConnectorManager.getConnectorPropertyEditorMouseSupportActivator()));
    }

    @Override
    public IDialogPropertyEditable<Process> createPropertyEditorDialog(JFrame owner, Process data) throws MoSGuiException {
        return new DialogProcessPropertyEditor(owner, data, false);
    }

    /****************************************** {@link IDataProvidable} ******************************/
    @Override
    public ProcessData getData() {
        return data;
    }

    public void setRank(int rank) {
        data.setRank(rank);
        setToolTipText("<HTML>Process<BR>rank: " + data.getRank() + "</HTML>");
    }

    /***************************************** {@link IParentRemovable} ******************************/
    @Override
    public void removeAction() {
        ConnectorManager.removeBySource(this);
        parent.removeChild(this);
        FrameMain.getOnlyInstance().repaint();
    }

    /****************************************** {@link IConnectableSource} ***************************/
    @Override
    public Process getSource() {
        return this;
    }

    @Override
    public Color getConnectorColor() {
        return IGuiConst.COLORS[data.getRank() % IGuiConst.COLORS.length];
    }

    @Override
    public List<SharedResource> getAvailableDestinations() {
        return sharedResources.getResources();
    }

    /****************************************** {@link IConnectable} **********************************/
    @Override
    public ConnectionPoint addConnection() {
        return connectorContainer.addConnectionPoint();
    }

    /*************************************** {@link IXmlProvideable} ****************
	 * @throws MoSXmlConfigurationException */
    @Override
    public Document prepareXml(Document xmlDocument, Element parentNode) throws MoSXmlConfigurationException {
        Element processNode = xmlDocument.createElement(IXmlTags.PROCESS_TAG);
        Element rankNode = xmlDocument.createElement(IXmlTags.PROCESS_RANK_TAG);
        rankNode.setTextContent(String.valueOf(data.getRank()));
        processNode.appendChild(rankNode);
        ownResources.prepareXml(xmlDocument, processNode);
        sharedResources.prepareXml(xmlDocument, processNode);
        int processConnectors = ConnectorManager.prepareXmlForSource(xmlDocument, processNode, this);
        if (sharedResources.isEmpty() && processConnectors < 1) throw new MoSXmlConfigurationException(MessageFormat.format("At least 1 shared resource or connector per process is required.\nCheck configuration of process nr: \"{0}\".", getData().getRank())); else if (!sharedResources.isEmpty() && processConnectors < 1 && !sharedResources.isAnyResourceUsed()) throw new MoSXmlConfigurationException(MessageFormat.format("At least 1 process shared resource must be used by other process.\nCheck configuration of process nr: \"{0}\".", getData().getRank()));
        parentNode.appendChild(processNode);
        return xmlDocument;
    }

    /****************************************** {@link Comparable} ************************************/
    @Override
    public int compareTo(Process p) {
        if (data.getRank() < p.getData().getRank()) return -1; else if (data.getRank() == p.getData().getRank()) return 0; else return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Process) {
                return data.getRank() == ((Process) obj).getData().getRank();
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return data.getRank();
    }
}
