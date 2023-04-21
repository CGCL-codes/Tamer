package com.julila.server;

import java.util.ArrayList;
import java.util.Date;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.julila.client.DataService;
import com.julila.domain.Sequence;
import com.julila.server.pm.SeqService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements DataService {

    @Override
    public Integer addSequence(Integer studentId, Date created, int index, ArrayList<Integer> seq) {
        try {
            System.out.println("creating sequence");
            Sequence s = new Sequence(studentId, created, seq);
            s.setLast(true);
            s.setIndexChanged(index);
            SeqService.addSequence(s);
            return 1;
        } catch (Exception e) {
        }
        return -1;
    }

    @Override
    public ArrayList<Integer> getSequence() {
        System.out.println("retreating sequence");
        return SeqService.getLastSequences();
    }
}
