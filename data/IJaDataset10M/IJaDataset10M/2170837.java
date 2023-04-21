package fr.alesia.deepstack.io.impl;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import fr.alesia.deepstack.bean.Ranking;
import fr.alesia.deepstack.io.BeanWriter;

public class RankingWriter implements BeanWriter<Ranking> {

    private static BeanWriter<Ranking> instance = null;

    private RankingWriter() {
        super();
    }

    public static BeanWriter<Ranking> getInstance() {
        if (instance == null) {
            instance = new RankingWriter();
        }
        return instance;
    }

    @Override
    public void write(OutputStream stream, Collection<Ranking> collection) throws Exception {
        OutputStreamWriter writer = new OutputStreamWriter(stream);
        BufferedWriter buffer = new BufferedWriter(writer);
        for (Ranking ranking : collection) {
            writer.write(ranking.getTournament().getId() + "," + ranking.getPlayer().getId() + "," + ranking.getRank() + "\n");
        }
        buffer.flush();
        writer.flush();
        buffer.close();
        writer.close();
        buffer = null;
        writer = null;
    }
}
