package nl.gridshore.samples.raffle.web.wicket.raffle;

import nl.gridshore.samples.raffle.business.RaffleService;
import nl.gridshore.samples.raffle.domain.Participant;
import nl.gridshore.samples.raffle.domain.Prize;
import nl.gridshore.samples.raffle.domain.Raffle;
import nl.gridshore.samples.raffle.web.wicket.BasePage;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Nov 23, 2007
 * Time: 9:31:28 PM
 * Page showing a specific Raffle with all the participants and winners
 */
public class ViewRafflePage extends BasePage {

    @SpringBean
    RaffleService raffleService;

    public ViewRafflePage(final PageParameters pageParams) {
        add(new Label("page-title-label", "View raffle"));
        Raffle raffle = raffleService.giveRaffleById(pageParams.getLong(RaffleConstants.PARAM_RAFFLE_ID));
        add(new Label("title-label", "Raffle title"));
        add(new Label("title", raffle.getTitle()));
        add(new Label("description-label", "Raffle description"));
        add(new Label("description", raffle.getDescription()));
        add(new Label("participant-name-label", "Participant name"));
        add(new ListView("participants", raffle.getParticipants()) {

            protected void populateItem(ListItem item) {
                Participant participant = (Participant) item.getModelObject();
                item.add(new Label("name", participant.getName()));
            }
        });
        add(new Label("prize-title-label", "Prize title"));
        add(new Label("prize-description-label", "Prize description"));
        add(new ListView("prizes", raffle.getPrizes()) {

            protected void populateItem(ListItem item) {
                Prize prize = (Prize) item.getModelObject();
                item.add(new Label("prize-title-value", prize.getTitle()));
                item.add(new Label("prize-description-value", prize.getDescription()));
            }
        });
        add(new Link("back") {

            public void onClick() {
                setResponsePage(AllRafflesPage.class);
            }
        });
    }
}
