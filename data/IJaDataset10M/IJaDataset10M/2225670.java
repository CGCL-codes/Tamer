package com.hack23.cia.service.impl.control.agent.sweden.impl.agents;

import java.util.ArrayList;
import java.util.List;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.hack23.cia.model.api.sweden.configuration.CommitteeData;
import com.hack23.cia.model.api.sweden.factory.SwedenModelFactory;
import com.hack23.cia.service.impl.control.agent.sweden.api.CommitteeAgent;

/**
 * The Class CommitteeAgentImpl.
 */
public class CommitteeAgentImpl extends AbstractParliamentDataAgentImpl<CommitteeData> implements CommitteeAgent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new committee agent impl.
	 * 
	 * @param webClient the web client
	 * @param swedenModelFactory the sweden model factory
	 */
    protected CommitteeAgentImpl(final WebClient webClient, final SwedenModelFactory swedenModelFactory) {
        super(webClient, swedenModelFactory);
    }

    @Override
    public final List<CommitteeData> getCurrentList() {
        final List<CommitteeData> committees = new ArrayList<CommitteeData>();
        try {
            final HtmlPage page = getWebClient().getPage("http://www.riksdagen.se/webbnav/index.aspx?nid=3320");
            final List<HtmlForm> forms = page.getForms();
            for (final HtmlForm htmlForm : forms) {
                if ("sform".equals(htmlForm.getAttribute("name"))) {
                    final HtmlSelect selectByName = htmlForm.getSelectByName("org");
                    if (selectByName != null) {
                        final List<HtmlOption> options = selectByName.getOptions();
                        for (final HtmlOption option : options) {
                            if (!"[välj utskott]".equals(option.asText())) {
                                committees.add(getSwedenModelFactory().createCommitteeData(option.asText(), option.getValueAttribute()));
                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return committees;
    }
}
