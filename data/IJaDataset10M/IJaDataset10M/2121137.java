package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.TruthsayerComplaint;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerComplaintDAO;

@TysanRankSecured(Rank.CHANCELLOR)
public class ChancellorTruthsayerComplaintPage extends AbstractMemberPage {

    public class DeferResponder extends DefaultClickResponder<TruthsayerComplaint> {

        private static final long serialVersionUID = 1L;

        public DeferResponder(TruthsayerComplaint c) {
            super(ModelMaker.wrap(c));
        }

        @Override
        public void onClick() {
            lawEnforcementService.complaintToSenate(getModelObject(), true);
            setResponsePage(new ChancellorTruthsayerComplaintPage());
        }
    }

    public class MediationResponder extends DefaultClickResponder<TruthsayerComplaint> {

        private static final long serialVersionUID = 1L;

        public MediationResponder(TruthsayerComplaint complaint) {
            super(ModelMaker.wrap(complaint));
        }

        @Override
        public void onClick() {
            lawEnforcementService.complaintMediated(getModelObject());
            setResponsePage(new ChancellorTruthsayerComplaintPage());
        }
    }

    @SpringBean
    private TruthsayerComplaintDAO complaintDAO;

    @SpringBean
    private LawEnforcementService lawEnforcementService;

    public ChancellorTruthsayerComplaintPage() {
        super("Truthsayer Complaints");
        Accordion accordion = new Accordion("accordion");
        accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
        accordion.setAutoHeight(false);
        accordion.add(new ListView<TruthsayerComplaint>("complaints", ModelMaker.wrap(complaintDAO.findAll())) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<TruthsayerComplaint> item) {
                TruthsayerComplaint c = item.getModelObject();
                lawEnforcementService.setComplaintObserved(c);
                item.add(new MemberListItem("complainer", c.getComplainer()));
                item.add(new MemberListItem("truthsayer", c.getTruthsayer()));
                item.add(new Label("title", "Complaint against " + c.getTruthsayer().getUsername()));
                item.add(new Label("complaint", c.getComplaint()).setEscapeModelStrings(false));
                boolean mediated = c.isMediated();
                item.add(new IconLink.Builder("images/icons/tick.png", new MediationResponder(c)).setText("This complaint has been resolved, and does not require a Senate vote").newInstance("mediated").setVisible(!mediated));
                item.add(new IconLink.Builder("images/icons/cross.png", new DeferResponder(c)).setText("This complaint is not resolved, the Senate should vote on this matter").newInstance("defer").setVisible(!mediated));
            }
        });
        add(accordion);
    }
}
