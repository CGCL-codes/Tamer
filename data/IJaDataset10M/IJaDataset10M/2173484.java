package org.xmi.infoset.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.xmi.infoset.XMIDocumentation;

public class XMIDocumentationImpl implements XMIDocumentation {

    private List<String> contact = new ArrayList<String>();

    private List<String> exporter = new ArrayList<String>();

    private List<String> exporterVersion = new ArrayList<String>();

    private List<String> exporterId = new ArrayList<String>();

    private List<String> longDescription = new ArrayList<String>();

    private List<String> shortDescription = new ArrayList<String>();

    private List<String> notice = new ArrayList<String>();

    private List<String> owner = new ArrayList<String>();

    public List<String> getContact() {
        return contact;
    }

    public void setContact(List<String> contact) {
        this.contact = contact;
    }

    public List<String> getExporter() {
        return exporter;
    }

    public void setExporter(List<String> exporter) {
        this.exporter = exporter;
    }

    public List<String> getExporterId() {
        return exporterId;
    }

    public void setExporterId(List<String> exporterId) {
        this.exporterId = exporterId;
    }

    public List<String> getExporterVersion() {
        return exporterVersion;
    }

    public void setExporterVersion(List<String> exporterVersion) {
        this.exporterVersion = exporterVersion;
    }

    public List<String> getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(List<String> longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getNotice() {
        return notice;
    }

    public void setNotice(List<String> notice) {
        this.notice = notice;
    }

    public List<String> getOwner() {
        return owner;
    }

    public void setOwner(List<String> owner) {
        this.owner = owner;
    }

    public List<String> getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(List<String> shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName()).append("[");
        buffer.append("\n\tcontact:").append(contact);
        buffer.append("\n\texporter:").append(exporter);
        buffer.append("\n\texporterId:").append(exporterId);
        buffer.append("\n\texporterVersion:").append(exporterVersion);
        buffer.append("\n\tshortDescription:").append(shortDescription);
        buffer.append("\n\tlongDescription:").append(longDescription);
        buffer.append("\n\tnotice:").append(notice);
        buffer.append("\n\towner:").append(owner);
        buffer.append("]");
        return buffer.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(539, 963).append(getContact()).append(getExporter()).append(getExporterId()).append(getExporterVersion()).append(getLongDescription()).append(getNotice()).append(getOwner()).append(getShortDescription()).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XMIDocumentationImpl == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        XMIDocumentation rhs = (XMIDocumentation) obj;
        return new EqualsBuilder().append(getContact(), rhs.getContact()).append(getExporter(), rhs.getExporter()).append(getExporterId(), rhs.getExporterId()).append(getExporterVersion(), rhs.getExporterVersion()).append(getLongDescription(), rhs.getLongDescription()).append(getNotice(), rhs.getNotice()).append(getOwner(), rhs.getOwner()).append(getShortDescription(), rhs.getShortDescription()).isEquals();
    }
}
