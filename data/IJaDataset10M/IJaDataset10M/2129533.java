package de.tum.in.eist.poll.server;

import java.util.Date;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import de.tum.in.eist.poll.client.StatService;
import de.tum.in.eist.poll.shared.Lecturer;
import de.tum.in.eist.poll.shared.Statistic;
import de.tum.in.eist.poll.shared.User;
import de.tum.in.eist.poll.shared.exceptions.InvalidAdminLoginException;
import de.tum.in.eist.poll.shared.exceptions.PollNotFoundException;

public class StatServiceImpl extends RemoteServiceServlet implements StatService {

    public Statistic getStatistic(User usr, Date pollDate) throws PollNotFoundException, InvalidAdminLoginException {
        if (!Database.pollDatabase.getPoll(pollDate).getEnd().before(new Date())) {
            try {
                Lecturer l = (Lecturer) usr;
            } catch (ClassCastException cce) {
                throw new InvalidAdminLoginException();
            }
        }
        return Database.pollDatabase.getStatistic(pollDate);
    }
}
