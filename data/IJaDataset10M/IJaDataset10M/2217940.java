package werewolf;

import javax.swing.JOptionPane;

public class Narratore {

    public void start() {
        String numero = JOptionPane.showInputDialog("Quanti siete a giocare?");
        int num = Integer.parseInt(numero);
        String[] giorni = { "Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom" };
        String[] totruoli = { "Medium", "Druido", "Veggente", "Meretrice", "Prete", "Vampiro", "Ficcanaso", "Lupo", "Guaritore", "Becchino", "Figlio dei lupi", "Amante", "Scemo del villaggio", "Contadino", "Sindaco", "Guardia" };
        String[] ruoli = new String[num];
        Giocatore[] players = new Giocatore[num];
        for (int i = 0; i < num; i++) {
            Object ruolo = JOptionPane.showInputDialog(null, "Inserire il " + (i + 1) + " ruolo", "Inserire ruolo", JOptionPane.QUESTION_MESSAGE, null, totruoli, totruoli[i]);
            while (ruolo.equals("RUOLO GIA' ASSEGNATO!")) {
                ruolo = JOptionPane.showInputDialog(null, "Inserire il " + (i + 1) + " ruolo", "Inserire ruolo", JOptionPane.QUESTION_MESSAGE, null, totruoli, totruoli[i]);
            }
            ruoli[i] = ruolo.toString();
            for (int id = 0; id < totruoli.length; id++) {
                if (ruoli[i].equals(totruoli[id])) {
                    if (!ruoli[i].equals("Lupo") && !ruoli[i].equals("Contadino") && !ruoli[i].equals("Scemo del villaggio") && !ruoli[i].equals("Guardia")) {
                        totruoli[id] = "RUOLO GIA' ASSEGNATO!";
                    }
                }
            }
        }
        int giorno = JOptionPane.showOptionDialog(null, "Inserire giorno della settimana iniziale", "Inserire Giorno", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, giorni, giorni[0]);
        int[] usciti = new int[num];
        for (int i = 0; i < num; i++) {
            usciti[i] = num + 1;
        }
        for (int i = 0; i < num; i++) {
            String nome = JOptionPane.showInputDialog("Inserire nome " + (i + 1) + " giocatore");
            while (nome.isEmpty()) {
                nome = JOptionPane.showInputDialog("Inserire nome " + (i + 1) + " giocatore");
            }
            int rand = func.func.rand(num - 1);
            while (func.func.isNumInArray(rand, usciti)) {
                rand = func.func.rand(num - 1);
            }
            usciti[i] = rand;
            String ruolo = ruoli[rand];
            players[i] = new Giocatore(nome, ruolo);
        }
        for (int i = 0; i < num; i++) {
            JOptionPane.showMessageDialog(null, "Apra gli occhi " + players[i].getNome() + " per scoprire il proprio ruolo!");
            JOptionPane.showMessageDialog(null, players[i].getNome() + " il tuo ruolo è: " + players[i].getRuolo());
        }
        int finita = 1;
        int conto = 1;
        boolean rogo = false;
        while (finita == 1) {
            String amante = "";
            String druido = "";
            String veggente = "";
            String meretrice = "";
            String vampiro = "";
            String ficcanaso = "";
            String lupo = "";
            boolean figlio = false;
            int contomorti = 0;
            int[] morti = new int[num];
            String[] nomi = new String[num];
            for (int i = 0; i < num; i++) {
                morti[i] = num + 1;
                if (players[i].isDead()) {
                    nomi[i] = "ABITANTE DEFUNTO";
                } else {
                    nomi[i] = players[i].getNome();
                }
            }
            if (conto == 1) {
                if (func.func.isRuoloIn(players, "Amante")) {
                    int id = func.func.getIdByRole(players, "Amante");
                    Object input = JOptionPane.showInputDialog(null, "Scegli il tuo amante!!", "Scelta amante", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    while (input.equals("ABITANTE DEFUNTO") || input.equals(players[id].getNome())) {
                        input = JOptionPane.showInputDialog(null, "Scegli il tuo amante!!", "Scelta amante", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    }
                    amante = input.toString();
                }
                if (func.func.isRuoloIn(players, "Guardie")) {
                    JOptionPane.showMessageDialog(null, "Guardie aprite gli occhi e riconoscetevi tra di voi!");
                }
                if (func.func.isRuoloIn(players, "Scemo del villaggio")) {
                    JOptionPane.showMessageDialog(null, "Scemo del villaggio apri gli occhi e riconosci i contadini");
                }
            }
            if (conto != 1) {
                if (func.func.isRuoloIn(players, "Medium")) {
                    int id = func.func.getIdByRole(players, "Medium");
                    JOptionPane.showMessageDialog(null, "Apra gli occhi il medium!!");
                    if (players[id].haPoteri()) {
                        if (rogo) {
                            JOptionPane.showMessageDialog(null, "E' andato a rogo un lupo");
                        } else {
                            JOptionPane.showMessageDialog(null, "Non è andato al rogo un lupo");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Mi dispiace ma non hai poteri!");
                    }
                }
            }
            if (func.func.isRuoloIn(players, "Druido")) {
                int idd = func.func.getIdByRole(players, "Druido");
                if (players[idd].haPoteri()) {
                    Object input = JOptionPane.showInputDialog(null, "Apra gli occhi il druido e mi indichi chi proteggere!", "Scelta druido", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    while (input.equals("ABITANTE DEFUNTO") || input.equals(players[idd].getNome())) {
                        input = JOptionPane.showInputDialog(null, "Apra gli occhi il druido e mi indichi chi proteggere!", "Scelta druido", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    }
                    druido = input.toString();
                    int id = func.func.getIdGiocatore(players, druido);
                    players[id].proteggi();
                } else {
                    JOptionPane.showMessageDialog(null, "Mi dispiace ma non hai poteri!");
                }
            }
            if (func.func.isRuoloIn(players, "Veggente")) {
                int idd = func.func.getIdByRole(players, "Veggente");
                if (players[idd].haPoteri()) {
                    Object input = JOptionPane.showInputDialog(null, "Apra gli occhi il veggente e mi indichi una persona per sapere se è il lupo!", "Scelta veggente", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    while (input.equals("ABITANTE DEFUNTO") || input.equals(players[idd].getNome())) {
                        input = JOptionPane.showInputDialog(null, "Apra gli occhi il veggente e mi indichi una persona per sapere se è il lupo", "Scelta veggente", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    }
                    veggente = input.toString();
                    int id = func.func.getIdGiocatore(players, veggente);
                    if (players[id].getRuolo().equals("Lupo")) {
                        JOptionPane.showMessageDialog(null, players[id].getNome() + " è un lupo");
                    } else {
                        JOptionPane.showMessageDialog(null, players[id].getNome() + " non è un lupo");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Mi dispiace non hai poteri!");
                }
            }
            if (func.func.isRuoloIn(players, "Meretrice")) {
                int id = func.func.getIdByRole(players, "Meretrice");
                if (players[id].haPoteri()) {
                    Object input = JOptionPane.showInputDialog(null, "Apra gli occhi la meretrice e mi indichi il cliente!", "Scelta cliente", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    while (input.equals("ABITANTE DEFUNTO") || input.equals(players[id].getNome())) {
                        input = JOptionPane.showInputDialog(null, "Apra gli occhi la meretrice e mi indichi il cliente", "Scelta cliente", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    }
                    meretrice = input.toString();
                    int idd = func.func.getIdGiocatore(players, meretrice);
                    if (players[idd].getRuolo().equals("Lupo")) {
                        players[id].ammazza();
                        players[id].setPoteri(false);
                        morti[contomorti] = id;
                        contomorti++;
                    } else {
                        players[idd].setPoteri(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Mi dispace non hai poteri!");
                }
            }
            if (func.func.isRuoloIn(players, "Vampiro")) {
                int idd = func.func.getIdByRole(players, "Vampiro");
                if (players[idd].haPoteri()) {
                    Object input = JOptionPane.showInputDialog(null, "Apra gli occhi il vampiro e mi indichi a chi succhiare il sangue!", "Scelta vampiro", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    while (input.equals("ABITANTE DEFUNTO") || input.equals(players[idd].getNome())) {
                        input = JOptionPane.showInputDialog(null, "Apra gli occhi il vampiro e mi indichi a chi succhiare il sangue", "Scelta vampiro", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    }
                    vampiro = input.toString();
                    int id = func.func.getIdGiocatore(players, vampiro);
                    if (players[id].getRuolo().equals("Prete")) {
                        players[id].setPoteri(false);
                    }
                    if (players[id].getRuolo().equals("Lupo")) {
                        players[id].ammazza();
                        players[id].setPoteri(false);
                        morti[contomorti] = id;
                        contomorti++;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Mi dispiace non hai poteri!");
                }
            }
            if (func.func.isRuoloIn(players, "Lupo")) {
                Object input = JOptionPane.showInputDialog(null, "Apra gli occhi il lupo e mi indichi chi far fuori!", "Scelta lupo", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                while (input.equals("ABITANTE DEFUNTO") || players[func.func.getIdGiocatore(players, lupo)].getRuolo().equals("Lupo")) {
                    input = JOptionPane.showInputDialog(null, "Apra gli occhi il lupo e mi indichi chi far fuori", "Scelta lupo", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                }
                lupo = input.toString();
                int id = func.func.getIdGiocatore(players, lupo);
                if (!players[id].isProtetto()) {
                    players[id].ammazza();
                    players[id].setPoteri(false);
                    morti[contomorti] = id;
                    contomorti++;
                }
                if (players[id].getRuolo().equals("Figlio dei lupi") && !players[id].isProtetto()) {
                    players[id].redivivi();
                    players[id].setPoteri(true);
                    contomorti--;
                    morti[contomorti] = num + 1;
                    figlio = true;
                    players[id].setRuolo("Lupo");
                }
            }
            if (func.func.isRuoloIn(players, "Ficcanaso")) {
                int idd = func.func.getIdByRole(players, "Ficcanaso");
                if (players[idd].haPoteri()) {
                    Object input = JOptionPane.showInputDialog(null, "Apra gli occhi il ficcanaso e mi indichi chi seguirà!", "Scelta ficcanaso", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    while (input.equals("ABITANTE DEFUNTO") || input.equals(players[idd].getNome())) {
                        input = JOptionPane.showInputDialog(null, "Apra gli occhi il ficcanaso e mi indichi chi seguirà", "Scelta ficcanaso", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                    }
                    ficcanaso = input.toString();
                    if (ficcanaso.equals(meretrice) || ficcanaso.equals(lupo)) {
                        int id = func.func.getIdGiocatore(players, ficcanaso);
                        JOptionPane.showMessageDialog(null, players[id].getNome() + " è un " + players[id].getRuolo());
                    }
                    int id = func.func.getIdByRole(players, "Lupo");
                    if (ficcanaso.equals(players[id].getNome())) {
                        players[idd].ammazza();
                        players[idd].setPoteri(false);
                        morti[contomorti] = idd;
                        contomorti++;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Mi dispiace non hai poteri");
                }
            }
            if (func.func.isRuoloIn(players, "Guaritore")) {
                int idd = func.func.getIdByRole(players, "Guaritore");
                if (players[idd].haPoteri()) {
                    int id = func.func.getIdGiocatore(players, lupo);
                    int salva = JOptionPane.showConfirmDialog(null, "Vuoi salvare " + players[id].getNome() + "?");
                    if (salva == 0) {
                        players[idd].setPoteri(false);
                        int idl = func.func.getIdGiocatore(players, lupo);
                        players[idl].redivivi();
                        players[idl].setPoteri(true);
                        if (figlio) {
                            players[idl].setRuolo("Figlio dei lupi");
                            figlio = false;
                        }
                    } else {
                        players[id].ammazza();
                        players[id].setPoteri(false);
                        morti[contomorti] = id;
                        contomorti++;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Mi dispiace non hai poteri");
                }
            }
            if (func.func.isRuoloIn(players, "Becchino")) {
                int idd = func.func.getIdByRole(players, "Becchino");
                if (players[idd].haPoteri()) {
                    int id = func.func.getIdGiocatore(players, lupo);
                    if (players[id].isDead()) {
                        JOptionPane.showMessageDialog(null, "E' morto il " + players[id].getRuolo());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Mi dispiace non hai poteri");
                }
            }
            int idd = func.func.getIdByRole(players, "Meretrice");
            int iddd = func.func.getIdByRole(players, "Amante");
            if (lupo.equals(players[idd].getNome())) {
                int id = func.func.getIdGiocatore(players, meretrice);
                players[id].ammazza();
                morti[contomorti] = id;
                contomorti++;
            }
            if (lupo.equals(amante) || lupo.equals(players[iddd].getNome())) {
                int id = func.func.getIdGiocatore(players, amante);
                players[id].ammazza();
                players[id].setPoteri(false);
                players[iddd].ammazza();
                players[iddd].setPoteri(false);
                morti[contomorti] = id;
                contomorti++;
                morti[contomorti] = iddd;
                contomorti++;
            }
            players[idd].setPoteri(true);
            giorno++;
            String dead = "";
            int deaded = 0;
            for (int i = 0; i < morti.length; i++) {
                if (morti[i] != num + 1) {
                    dead = dead + players[morti[i]].getNome() + ", ";
                    deaded++;
                }
            }
            for (int i = 0; i < num; i++) {
                if (players[i].isDead()) {
                    nomi[i] = "ABITANTE DEFUNTO";
                } else {
                    nomi[i] = players[i].getNome();
                }
            }
            if (giorno > 6) {
                giorno = 0;
            }
            if (deaded > 0) {
                JOptionPane.showMessageDialog(null, "E' giorno, è " + giorni[giorno] + " e sono morti " + dead + " discutete!");
            } else if (deaded == 1) {
                JOptionPane.showMessageDialog(null, "E' giorno, è " + giorni[giorno] + " ed è morto " + dead + " discutete!");
            } else {
                JOptionPane.showMessageDialog(null, "E' giorno, è " + giorni[giorno] + " e non è morto nessuno!");
            }
            if (figlio) {
                JOptionPane.showMessageDialog(null, "C'è un lupo in più!!");
            }
            if ((func.func.numLupi(players) >= func.func.numCont(players))) {
                Object input = JOptionPane.showInputDialog(null, "Chi volete mandare al rogo?", "Rogo", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                String burn = input.toString();
                int salva = 0;
                int id = func.func.getIdGiocatore(players, burn);
                if (giorni[giorno].equals("Dom")) {
                    if (func.func.isRuoloIn(players, "Prete")) {
                        int idddd = func.func.getIdByRole(players, "Prete");
                        if (players[idddd].haPoteri()) {
                            salva = JOptionPane.showConfirmDialog(null, "E' domenica, il prete vuole farsi avanti e mandare al rogo un'altra persona?");
                            if (salva == 0) {
                                Object inputp = JOptionPane.showInputDialog(null, "Chi vuoi mandare al rogo?", "Rogo", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);
                                String burnp = inputp.toString();
                                int idp = func.func.getIdGiocatore(players, burnp);
                                players[idp].ammazza();
                                players[idp].setPoteri(false);
                            }
                        }
                    }
                }
                int idddd = func.func.getIdByRole(players, "Sindaco");
                if (func.func.isRuoloIn(players, "Sindaco")) {
                    if (players[idddd].haPoteri()) {
                        salva = JOptionPane.showConfirmDialog(null, "Il sindaco vuole farsi avanti e savare " + players[id] + "?");
                    }
                } else {
                    salva = 1;
                }
                if (salva == 0) {
                    players[idddd].setPoteri(false);
                } else {
                    players[id].ammazza();
                    players[id].setPoteri(false);
                    if (players[id].getRuolo().equals("Lupo")) {
                        rogo = true;
                    }
                }
            }
            if (func.func.numCont(players) == func.func.numLupi(players)) {
                JOptionPane.showMessageDialog(null, "I lupi hanno vinto!");
                finita = 0;
            }
            if (func.func.numLupi(players) == 0) {
                JOptionPane.showMessageDialog(null, "I contadini hanno vinto!");
                finita = 0;
            }
            conto++;
        }
    }
}
