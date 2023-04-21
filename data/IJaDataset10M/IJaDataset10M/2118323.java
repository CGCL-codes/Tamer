package br.eng.eliseu.j2me.radar.forms;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import br.eng.eliseu.j2me.radar.db.RadarQuery;
import br.eng.eliseu.j2me.radar.db.RadarRegistro;
import br.eng.eliseu.j2me.radar.db.RodoviaQuery;
import br.eng.eliseu.j2me.radar.db.RodoviaRegistro;

public class FormMostraRadar extends Canvas implements CommandListener {

    private Radar hMain;

    private RodoviaRegistro tblRodovia;

    private int idRodovia;

    private int sentido;

    private RadarRegistro tblRadar = new RadarRegistro();

    private Command cmdVoltar, cmdEditar, cmdNovo, cmdExcluir;

    private String joistic;

    private CampoTesto dfOrigem, dfDestino, dfSentido;

    private CampoTesto dfKmAnt, dfKmApos, dfKm, dfTipo, dfMouse;

    private int indiceRadar;

    private RadarRegistro[] rad = null;

    public FormMostraRadar(Radar hMain, RodoviaRegistro tblRodovia, int idRodovia, int sentido) {
        this.hMain = hMain;
        this.tblRodovia = tblRodovia;
        this.idRodovia = idRodovia;
        this.sentido = sentido;
        cmdVoltar = new Command("Voltar", Command.EXIT, 1);
        cmdEditar = new Command("Editar", Command.SCREEN, 1);
        cmdNovo = new Command("Nova", Command.SCREEN, 2);
        cmdExcluir = new Command("Excluir", Command.SCREEN, 3);
        dfOrigem = new CampoTesto(getWidth() / 2, 12);
        dfDestino = new CampoTesto(getWidth() / 2, 30);
        dfSentido = new CampoTesto(getWidth() / 2, 50);
        dfKmAnt = new CampoTesto(getWidth() / 2, 70);
        dfKm = new CampoTesto(getWidth() / 2, getHeight() / 2);
        dfTipo = new CampoTesto(getWidth() / 2, (getHeight() / 2) + 40);
        dfMouse = new CampoTesto(getWidth() / 2, (getHeight() / 2) + 60);
        dfKmApos = new CampoTesto(getWidth() / 2, (getHeight() / 2) + 80);
        addCommand(cmdVoltar);
        addCommand(cmdEditar);
        addCommand(cmdNovo);
        addCommand(cmdExcluir);
        setCommandListener(this);
        RodoviaRegistro[] rod = RodoviaQuery.selectRodoviaPorId(tblRodovia.getRS(), idRodovia);
        indiceRadar = 1;
        dfOrigem.setValor("ORIG: " + rod[0].getOrigem());
        dfDestino.setValor("DEST: " + rod[0].getDestino());
        dfSentido.setValor("Sentido: " + (sentido == 0 ? "IDA" : "VOLTA"));
        rad = RadarQuery.selectTodosRadar_PorKm(tblRadar.getRS(), idRodovia, sentido);
    }

    protected void paint(Graphics g) {
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        populaTela(g, indiceRadar);
        dfMouse.desenhaValor(g, Font.SIZE_MEDIUM, joistic);
    }

    private void populaTela(Graphics g, int npIndice) {
        dfOrigem.desenhaValor(g, Font.SIZE_LARGE, dfOrigem.getValor());
        dfDestino.desenhaValor(g, Font.SIZE_LARGE, dfDestino.getValor());
        dfSentido.desenhaValor(g, Font.SIZE_LARGE, dfSentido.getValor());
        dfKmAnt.desenhaValor(g, Font.SIZE_SMALL, dfKmAnt.getValor());
        dfKm.desenhaValor(g, Font.SIZE_LARGE, dfKm.getValor());
        dfTipo.desenhaValor(g, Font.SIZE_LARGE, dfTipo.getValor());
        dfKmApos.desenhaValor(g, Font.SIZE_SMALL, dfKmApos.getValor());
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdEditar) {
        } else if (c == cmdNovo) {
        } else if (c == cmdExcluir) {
        } else if (c == cmdVoltar) {
            this.hMain.telas.retiraTela();
        }
    }

    protected void keyPressed(int keyCode) {
        joistic = getKeyName(keyCode);
        if (rad != null) {
            if (joistic.equals("DOWN")) {
                indiceRadar++;
                if (indiceRadar >= rad.length) {
                    indiceRadar = rad.length - 1;
                }
            } else if (joistic.equals("UP")) {
                indiceRadar--;
                if (indiceRadar < 0) {
                    indiceRadar = 0;
                }
            }
            dfKm.setValor("KM: " + rad[indiceRadar].getKm());
            dfTipo.setValor(rad[indiceRadar].getTipo());
            repaint();
        }
    }
}
