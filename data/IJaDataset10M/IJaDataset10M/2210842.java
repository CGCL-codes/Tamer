package it.unibz.izock.client;

import java.util.ArrayList;
import java.util.List;
import it.unibz.izock.Cardable;
import it.unibz.izock.Color;
import it.unibz.izock.Schlag;
import it.unibz.izock.client.models.Models;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class DeveloperApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private String _perspective;

    private class SimpleCard implements Cardable {

        private Color _color;

        private Schlag _schlag;

        public SimpleCard(Color color, Schlag schlag) {
            _color = color;
            _schlag = schlag;
        }

        public Color getColor() {
            return _color;
        }

        public Schlag getSchlag() {
            return _schlag;
        }
    }

    public DeveloperApplicationWorkbenchAdvisor(String perspective) {
        _perspective = perspective;
        Models.server().setUsername("user1");
        List<String> seats = new ArrayList<String>();
        seats.add("user1");
        seats.add("user2");
        seats.add("user3");
        seats.add("user4");
        Models.gameBoard().setSeats(seats);
        List<Cardable> cards = new ArrayList<Cardable>();
        cards.add(new SimpleCard(Color.EICHEL, Schlag.ACHT));
        cards.add(new SimpleCard(Color.EICHEL, Schlag.NEUN));
        cards.add(new SimpleCard(Color.EICHEL, Schlag.ZEHN));
        cards.add(new SimpleCard(Color.EICHEL, Schlag.UNTER));
        cards.add(new SimpleCard(Color.EICHEL, Schlag.OBER));
        Models.hand().setCards(cards);
        Models.announcer().setColor(new SimpleCard(Color.HERZ, Schlag.ASS));
        Models.announcer().setSchlag(new SimpleCard(Color.LAUB, Schlag.ZEHN));
    }

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    @Override
    public String getInitialWindowPerspectiveId() {
        return _perspective;
    }
}
