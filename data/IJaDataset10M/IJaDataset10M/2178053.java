package org.dasein.cloud.aws.network;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeSet;
import java.util.UUID;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.ProviderContext;
import org.dasein.cloud.aws.AWSCloud;
import org.dasein.cloud.aws.compute.EC2Exception;
import org.dasein.cloud.identity.ServiceAction;
import org.dasein.cloud.network.DNSRecord;
import org.dasein.cloud.network.DNSRecordType;
import org.dasein.cloud.network.DNSSupport;
import org.dasein.cloud.network.DNSZone;
import org.dasein.util.Jiterator;
import org.dasein.util.JiteratorPopulator;
import org.dasein.util.PopulatorThread;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Route53 implements DNSSupport {

    private AWSCloud provider;

    Route53(AWSCloud provider) {
        this.provider = provider;
    }

    @Nonnull
    private String generateCallerReference() {
        return UUID.randomUUID().toString();
    }

    @Override
    @Nonnull
    public DNSRecord addDnsRecord(@Nonnull String providerDnsZoneId, @Nonnull DNSRecordType recordType, @Nonnull String name, @Nonnegative int ttl, @Nonnull String... values) throws CloudException, InternalException {
        Route53Method method;
        for (DNSRecord record : listDnsRecords(providerDnsZoneId, recordType, name)) {
            if (record != null) {
                deleteDnsRecords(record);
            }
        }
        method = new Route53Method(Route53Method.CHANGE_RESOURCE_RECORD_SETS, provider, getResourceUrl(providerDnsZoneId));
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        xml.append("<ChangeResourceRecordSetsRequest xmlns=\"https://route53.amazonaws.com/doc/2010-10-01/\">");
        xml.append("<ChangeBatch>");
        xml.append("<Changes>");
        xml.append("<Change>");
        xml.append("<Action>CREATE</Action>");
        xml.append("<ResourceRecordSet>");
        xml.append("<Name>");
        xml.append(name);
        xml.append("</Name>");
        xml.append("<Type>");
        xml.append(recordType.toString());
        xml.append("</Type>");
        xml.append("<TTL>");
        xml.append(String.valueOf(ttl));
        xml.append("</TTL>");
        xml.append("<ResourceRecords>");
        if (values.length > 0) {
            for (String value : values) {
                xml.append("<ResourceRecord>");
                xml.append("<Value>");
                xml.append(AWSCloud.escapeXml(value));
                xml.append("</Value>");
                xml.append("</ResourceRecord>");
            }
        }
        xml.append("</ResourceRecords>");
        xml.append("</ResourceRecordSet>");
        xml.append("</Change>");
        xml.append("</Changes>");
        xml.append("</ChangeBatch>");
        xml.append("</ChangeResourceRecordSetsRequest>");
        try {
            method.invoke(xml.toString(), false);
        } catch (EC2Exception e) {
            throw new CloudException(e);
        }
        for (DNSRecord record : listDnsRecords(providerDnsZoneId, recordType, name)) {
            if (record != null) {
                return record;
            }
        }
        throw new CloudException("Unable to identified newly added record");
    }

    @Override
    @Nonnull
    public String createDnsZone(@Nonnull String domainName, @Nonnull String name, @Nonnull String description) throws CloudException, InternalException {
        ProviderContext ctx = provider.getContext();
        if (ctx == null) {
            throw new CloudException("No context was configured for this request");
        }
        Route53Method method;
        NodeList blocks;
        Document doc;
        method = new Route53Method(Route53Method.CREATE_HOSTED_ZONE, provider, getHostedZoneUrl(null));
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        xml.append("<CreateHostedZoneRequest xmlns=\"https://route53.amazonaws.com/doc/2010-10-01/\">");
        xml.append("<Name>");
        xml.append(domainName);
        xml.append("</Name>");
        xml.append("<CallerReference>");
        xml.append(generateCallerReference());
        xml.append("</CallerReference>");
        xml.append("<HostedZoneConfig><Comment>");
        xml.append(AWSCloud.escapeXml(description));
        xml.append("</Comment></HostedZoneConfig>");
        xml.append("</CreateHostedZoneRequest>");
        try {
            doc = method.invoke(xml.toString(), false);
        } catch (EC2Exception e) {
            throw new CloudException(e);
        }
        ArrayList<String> ns = new ArrayList<String>();
        blocks = doc.getElementsByTagName("NameServer");
        for (int i = 0; i < blocks.getLength(); i++) {
            Node item = blocks.item(i);
            ns.add(item.getFirstChild().getNodeValue().trim());
        }
        String[] nameservers = new String[ns.size()];
        ns.toArray(nameservers);
        blocks = doc.getElementsByTagName("HostedZone");
        for (int i = 0; i < blocks.getLength(); i++) {
            Node item = blocks.item(i);
            DNSZone zone = toDnsZone(ctx, item, nameservers);
            if (zone != null) {
                return zone.getProviderDnsZoneId();
            }
        }
        throw new CloudException("Unable to identify newly created zone");
    }

    @Override
    public void deleteDnsRecords(@Nonnull DNSRecord... dnsRecords) throws CloudException, InternalException {
        if (dnsRecords.length < 1) {
            return;
        }
        TreeSet<String> zones = new TreeSet<String>();
        for (DNSRecord record : dnsRecords) {
            zones.add(record.getProviderZoneId());
        }
        for (String zoneId : zones) {
            Route53Method method = new Route53Method(Route53Method.CHANGE_RESOURCE_RECORD_SETS, provider, getResourceUrl(zoneId));
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
            xml.append("<ChangeResourceRecordSetsRequest xmlns=\"https://route53.amazonaws.com/doc/2010-10-01/\">");
            xml.append("<ChangeBatch>");
            xml.append("<Changes>");
            for (DNSRecord record : dnsRecords) {
                if (record.getProviderZoneId().equals(zoneId)) {
                    xml.append("<Change>");
                    xml.append("<Action>DELETE</Action>");
                    xml.append("<ResourceRecordSet>");
                    xml.append("<Name>");
                    xml.append(record.getName());
                    xml.append("</Name>");
                    xml.append("<Type>");
                    xml.append(record.getType().toString());
                    xml.append("</Type>");
                    xml.append("<TTL>");
                    xml.append(String.valueOf(record.getTtl()));
                    xml.append("</TTL>");
                    xml.append("<ResourceRecords>");
                    String[] values = record.getValues();
                    if (values != null && values.length > 0) {
                        for (String value : values) {
                            xml.append("<ResourceRecord>");
                            xml.append("<Value>");
                            xml.append(AWSCloud.escapeXml(value));
                            xml.append("</Value>");
                            xml.append("</ResourceRecord>");
                        }
                    }
                    xml.append("</ResourceRecords>");
                    xml.append("</ResourceRecordSet>");
                    xml.append("</Change>");
                }
            }
            xml.append("</Changes>");
            xml.append("</ChangeBatch>");
            xml.append("</ChangeResourceRecordSetsRequest>");
            try {
                method.invoke(xml.toString(), false);
            } catch (EC2Exception e) {
                throw new CloudException(e);
            }
        }
    }

    @Override
    public void deleteDnsZone(@Nonnull String providerDnsZoneId) throws CloudException, InternalException {
        Route53Method method;
        method = new Route53Method(Route53Method.DELETE_HOSTED_ZONE, provider, getHostedZoneUrl(providerDnsZoneId));
        try {
            method.invoke(false);
        } catch (EC2Exception e) {
            throw new CloudException(e);
        }
    }

    @Override
    @Nullable
    public DNSZone getDnsZone(@Nonnull String providerDnsZoneId) throws CloudException, InternalException {
        ProviderContext ctx = provider.getContext();
        if (ctx == null) {
            throw new CloudException("No context was configured for this request");
        }
        Route53Method method;
        NodeList blocks;
        Document doc;
        method = new Route53Method(Route53Method.GET_HOSTED_ZONE, provider, getHostedZoneUrl(providerDnsZoneId));
        try {
            doc = method.invoke(false);
        } catch (EC2Exception e) {
            String code = e.getCode();
            if (code != null && code.equals("AccessDenied")) {
                for (DNSZone zone : listDnsZones()) {
                    if (zone.getProviderDnsZoneId().equals(providerDnsZoneId)) {
                        return zone;
                    }
                }
                return null;
            }
            throw new CloudException(e);
        }
        ArrayList<String> ns = new ArrayList<String>();
        blocks = doc.getElementsByTagName("NameServer");
        for (int i = 0; i < blocks.getLength(); i++) {
            Node item = blocks.item(i);
            ns.add(item.getFirstChild().getNodeValue().trim());
        }
        String[] nameservers = new String[ns.size()];
        ns.toArray(nameservers);
        blocks = doc.getElementsByTagName("HostedZone");
        for (int i = 0; i < blocks.getLength(); i++) {
            Node item = blocks.item(i);
            DNSZone zone = toDnsZone(ctx, item, nameservers);
            if (zone != null) {
                return zone;
            }
        }
        return null;
    }

    @Override
    @Nonnull
    public String getProviderTermForRecord(@Nonnull Locale locale) {
        return "resource";
    }

    @Override
    @Nonnull
    public String getProviderTermForZone(@Nonnull Locale locale) {
        return "hosted zone";
    }

    @Nonnull
    private String getHostedZoneUrl(@Nullable String zoneId) {
        if (zoneId == null) {
            return "https://route53.amazonaws.com/" + AWSCloud.ROUTE53_VERSION + "/hostedzone";
        } else {
            return "https://route53.amazonaws.com/" + AWSCloud.ROUTE53_VERSION + "/hostedzone/" + zoneId;
        }
    }

    @Nonnull
    private String getResourceUrl(@Nonnull String zoneId) {
        return "https://route53.amazonaws.com/" + AWSCloud.ROUTE53_VERSION + "/hostedzone/" + zoneId + "/rrset";
    }

    @Override
    @Nonnull
    public Iterable<DNSRecord> listDnsRecords(@Nonnull String providerDnsZoneId, @Nullable DNSRecordType forType, @Nullable String name) throws CloudException, InternalException {
        final ProviderContext ctx = provider.getContext();
        if (ctx == null) {
            throw new CloudException("No context was configured for this request");
        }
        PopulatorThread<DNSRecord> populator;
        final String zoneId = providerDnsZoneId;
        final DNSRecordType type = forType;
        final String nom = name;
        provider.hold();
        populator = new PopulatorThread<DNSRecord>(new JiteratorPopulator<DNSRecord>() {

            public void populate(Jiterator<DNSRecord> iterator) throws CloudException, InternalException {
                populateRecords(iterator, zoneId, type, nom);
                provider.release();
            }
        });
        populator.populate();
        return populator.getResult();
    }

    private void populateRecords(@Nonnull Jiterator<DNSRecord> iterator, @Nonnull String providerDnsZoneId, @Nullable DNSRecordType forType, @Nullable String name) throws CloudException, InternalException {
        String url = getResourceUrl(providerDnsZoneId);
        Route53Method method;
        NodeList blocks;
        Document doc;
        if (forType == null) {
            if (name != null) {
                url = url + "?name=" + name;
            }
        } else {
            url = url + "?type=" + forType.toString();
            if (name != null) {
                url = url + "&name=" + name;
            }
        }
        method = new Route53Method(Route53Method.LIST_RESOURCE_RECORD_SETS, provider, url);
        try {
            doc = method.invoke(false);
        } catch (EC2Exception e) {
            throw new CloudException(e);
        }
        blocks = doc.getElementsByTagName("ResourceRecordSet");
        for (int i = 0; i < blocks.getLength(); i++) {
            Node item = blocks.item(i);
            DNSRecord record = toDnsRecord(providerDnsZoneId, item);
            if (record != null) {
                iterator.push(record);
            }
        }
        blocks = doc.getElementsByTagName("IsTruncated");
        if (blocks != null && blocks.getLength() == 1 && blocks.item(0).hasChildNodes() && blocks.item(0).getFirstChild().getNodeValue().trim().equalsIgnoreCase("true")) {
            DNSRecordType nextType = null;
            String nextName = null;
            blocks = doc.getElementsByTagName("NextRecordName");
            if (blocks != null && blocks.getLength() == 1 && blocks.item(0).hasChildNodes()) {
                nextName = blocks.item(0).getFirstChild().getNodeValue().trim();
            }
            blocks = doc.getElementsByTagName("NextRecordType");
            if (blocks != null && blocks.getLength() == 1 && blocks.item(0).hasChildNodes()) {
                nextType = DNSRecordType.valueOf(blocks.item(0).getFirstChild().getNodeValue().trim());
            }
            if (nextName != null && nextType != null) {
                populateRecords(iterator, providerDnsZoneId, nextType, nextName);
            }
        }
    }

    @Override
    @Nonnull
    public Iterable<DNSZone> listDnsZones() throws CloudException, InternalException {
        final ProviderContext ctx = provider.getContext();
        if (ctx == null) {
            throw new CloudException("No context was configured for this request");
        }
        PopulatorThread<DNSZone> populator;
        provider.hold();
        populator = new PopulatorThread<DNSZone>(new JiteratorPopulator<DNSZone>() {

            public void populate(Jiterator<DNSZone> iterator) throws CloudException, InternalException {
                populateZones(ctx, iterator, null);
                provider.release();
            }
        });
        populator.populate();
        return populator.getResult();
    }

    @Override
    @Nonnull
    public String[] mapServiceAction(@Nonnull ServiceAction action) {
        if (action.equals(DNSSupport.ANY)) {
            return new String[] { Route53Method.R53_PREFIX + "*" };
        } else if (action.equals(DNSSupport.ADD_RECORD)) {
            return new String[] { Route53Method.R53_PREFIX + Route53Method.CHANGE_RESOURCE_RECORD_SETS };
        } else if (action.equals(DNSSupport.CREATE_ZONE)) {
            return new String[] { Route53Method.R53_PREFIX + Route53Method.CREATE_HOSTED_ZONE };
        } else if (action.equals(DNSSupport.GET_ZONE)) {
            return new String[] { Route53Method.R53_PREFIX + Route53Method.GET_HOSTED_ZONE };
        } else if (action.equals(DNSSupport.LIST_ZONE)) {
            return new String[] { Route53Method.R53_PREFIX + Route53Method.LIST_HOSTED_ZONES };
        } else if (action.equals(DNSSupport.LIST_RECORD)) {
            return new String[] { Route53Method.R53_PREFIX + Route53Method.LIST_RESOURCE_RECORD_SETS };
        } else if (action.equals(DNSSupport.REMOVE_RECORD)) {
            return new String[] { Route53Method.R53_PREFIX + Route53Method.CHANGE_RESOURCE_RECORD_SETS };
        } else if (action.equals(DNSSupport.REMOVE_ZONE)) {
            return new String[] { Route53Method.R53_PREFIX + Route53Method.DELETE_HOSTED_ZONE };
        }
        return new String[0];
    }

    private void populateZones(@Nonnull ProviderContext ctx, @Nonnull Jiterator<DNSZone> iterator, @Nullable String marker) throws CloudException, InternalException {
        String url = getHostedZoneUrl(null);
        Route53Method method;
        NodeList blocks;
        Document doc;
        if (marker != null) {
            url = url + "?marker=" + marker;
        }
        method = new Route53Method(Route53Method.LIST_HOSTED_ZONES, provider, url);
        try {
            doc = method.invoke(false);
        } catch (EC2Exception e) {
            throw new CloudException(e);
        }
        blocks = doc.getElementsByTagName("HostedZone");
        for (int i = 0; i < blocks.getLength(); i++) {
            Node item = blocks.item(i);
            DNSZone zone = toDnsZone(ctx, item, new String[0]);
            if (zone != null) {
                iterator.push(zone);
            }
        }
        blocks = doc.getElementsByTagName("IsTruncated");
        if (blocks != null && blocks.getLength() == 1 && blocks.item(0).hasChildNodes() && blocks.item(0).getFirstChild().getNodeValue().trim().equalsIgnoreCase("true")) {
            blocks = doc.getElementsByTagName("NextMarker");
            if (blocks != null && blocks.getLength() == 1 && blocks.item(0).hasChildNodes()) {
                populateZones(ctx, iterator, blocks.item(0).getFirstChild().getNodeValue().trim());
            }
        }
    }

    @Override
    public boolean isSubscribed() throws CloudException, InternalException {
        Route53Method method;
        method = new Route53Method(Route53Method.LIST_HOSTED_ZONES, provider, getHostedZoneUrl(null));
        try {
            method.invoke(false);
        } catch (EC2Exception e) {
            if (e.getStatus() == HttpServletResponse.SC_UNAUTHORIZED || e.getStatus() == HttpServletResponse.SC_FORBIDDEN) {
                return false;
            }
            String code = e.getCode();
            if (code != null && (code.equals("SubscriptionCheckFailed") || code.equals("AuthFailure") || code.equals("SignatureDoesNotMatch") || code.equals("InvalidClientTokenId") || code.equals("OptInRequired"))) {
                return false;
            }
            throw new CloudException(e);
        }
        return true;
    }

    @Nullable
    private DNSRecord toDnsRecord(@Nonnull String providerDnsZoneId, @Nullable Node xmlRecord) {
        if (xmlRecord == null) {
            return null;
        }
        NodeList attrs = xmlRecord.getChildNodes();
        DNSRecord record = new DNSRecord();
        record.setProviderZoneId(providerDnsZoneId);
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            String name;
            name = attr.getNodeName();
            if (name.equalsIgnoreCase("name")) {
                String value = attr.getFirstChild().getNodeValue();
                if (value == null) {
                    return null;
                }
                record.setName(value.trim());
            } else if (name.equalsIgnoreCase("type")) {
                String value = attr.getFirstChild().getNodeValue();
                if (value != null) {
                    record.setType(DNSRecordType.valueOf(value.trim()));
                }
            } else if (name.equalsIgnoreCase("ttl")) {
                String value = attr.getFirstChild().getNodeValue();
                if (value != null) {
                    record.setTtl(Integer.parseInt(value.trim()));
                }
            } else if (name.equalsIgnoreCase("resourcerecords")) {
                ArrayList<String> data = new ArrayList<String>();
                NodeList configs = attr.getChildNodes();
                for (int j = 0; j < configs.getLength(); j++) {
                    Node item = configs.item(j);
                    if (item.getNodeName().equalsIgnoreCase("resourcerecord")) {
                        NodeList values = item.getChildNodes();
                        for (int k = 0; k < values.getLength(); k++) {
                            Node r = values.item(k);
                            if (r.getNodeName().equalsIgnoreCase("value")) {
                                String value = (r.hasChildNodes() ? r.getFirstChild().getNodeValue() : null);
                                if (value != null) {
                                    data.add(value.trim());
                                }
                            }
                        }
                    }
                }
                record.setValues(data.toArray(new String[data.size()]));
            }
        }
        return record;
    }

    @Nullable
    private DNSZone toDnsZone(@Nonnull ProviderContext ctx, @Nullable Node xmlZone, @Nullable String[] nameservers) {
        if (xmlZone == null) {
            return null;
        }
        NodeList attrs = xmlZone.getChildNodes();
        DNSZone zone = new DNSZone();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            String name;
            name = attr.getNodeName();
            if (name.equalsIgnoreCase("id")) {
                String value = attr.getFirstChild().getNodeValue().trim();
                if (value == null) {
                    return null;
                }
                int idx = value.lastIndexOf('/');
                value = value.substring(idx + 1);
                zone.setProviderDnsZoneId(value);
            } else if (name.equalsIgnoreCase("name")) {
                String value = attr.getFirstChild().getNodeValue().trim();
                zone.setDomainName(value);
            } else if (name.equalsIgnoreCase("config")) {
                NodeList configs = attr.getChildNodes();
                for (int j = 0; j < configs.getLength(); j++) {
                    Node item = configs.item(j);
                    if (item.getNodeName().equalsIgnoreCase("comment")) {
                        zone.setDescription(item.getFirstChild().getNodeValue().trim());
                    }
                }
            }
        }
        if (zone.getName() == null) {
            zone.setName(zone.getDomainName());
        }
        if (zone.getDescription() == null) {
            zone.setDescription(zone.getName());
        }
        zone.setProviderOwnerId(ctx.getAccountNumber());
        zone.setNameservers(nameservers);
        return zone;
    }
}
