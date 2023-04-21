package org.chernovia.net.games.parlour.acro.iccserv;

public class AcroPlayer {

    class AcroRec {

        String acro;

        int vote;

        int votes;

        public AcroRec(String S, int v, int p) {
            acro = S;
            vote = v;
            votes = p;
            if (AcroBot.G.REVEAL && vote >= 0) {
                AcroBot.Serv.tch(AcroBot.chan, name + " voted for: " + AcroBot.G.players[vote].name);
            }
        }
    }

    String name;

    AcroRec record[];

    int acros, vote, score;

    public AcroPlayer(String n) {
        name = n;
        acros = 0;
        score = 0;
        vote = -1;
        record = new AcroRec[AcroBot.MAXACRO];
    }

    public void save(String a, int v, int p) {
        record[acros] = new AcroRec(a, v, p);
        if (acros < AcroBot.MAXACRO) acros++;
    }

    public int sumacros() {
        int a = 0;
        for (int i = 0; i < acros; i++) if (!record[i].acro.equals("")) a++;
        return a;
    }
}
