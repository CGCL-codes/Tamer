package net.sourceforge.symba.webapp.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.sourceforge.fuge.common.description.Description;
import net.sourceforge.symba.webapp.client.services.DescribableService;
import net.sourceforge.symba.webapp.client.util.fuge.common.DescribableClient;
import net.sourceforge.symba.webapp.server.generetedJAXB2.FuGECommonDescribableType;
import net.sourceforge.symba.webapp.server.generetedJAXB2.FuGECommonDescriptionDescriptionType;
import net.sourceforge.symba.webapp.server.generetedJAXB2.FuGEBioInvestigationInvestigationType;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: craig Date: 23-Sep-2009 Time: 21:00:59 To change this template use File | Settings |
 * File Templates.
 */
public class DescribableServiceImpl extends RemoteServiceServlet implements DescribableService {

    public DescribableClient createDescribable(final List<String> descriptions, final DescribableClient client) {
        client.setDescriptions(descriptions);
        return client;
    }

    public DescribableClient createDescribable(final Collection<Description> descriptions, final DescribableClient client) {
        final List<String> descriptionsString = new LinkedList<String>();
        for (Description d : descriptions) {
            descriptionsString.add(d.getText());
        }
        client.setDescriptions(descriptionsString);
        return client;
    }

    public DescribableClient createDescribable(final FuGEBioInvestigationInvestigationType.Descriptions descriptions, DescribableClient client) {
        final Collection<FuGECommonDescriptionDescriptionType> descriptionsXml = descriptions.getDescription();
        final List<String> descriptionsString = new LinkedList<String>();
        for (FuGECommonDescriptionDescriptionType d : descriptionsXml) {
            descriptionsString.add(d.getText());
        }
        client.setDescriptions(descriptionsString);
        return client;
    }

    public FuGECommonDescribableType createDescribable(final List<String> descriptions, final FuGECommonDescribableType describable) {
        final FuGECommonDescribableType.Descriptions descriptionsXml = new FuGECommonDescribableType.Descriptions();
        final List<FuGECommonDescriptionDescriptionType> descriptionListXml = descriptionsXml.getDescription();
        for (String s : descriptions) {
            final FuGECommonDescriptionDescriptionType description = new FuGECommonDescriptionDescriptionType();
            description.setText(s);
            descriptionListXml.add(description);
        }
        describable.setDescriptions(descriptionsXml);
        return describable;
    }
}
