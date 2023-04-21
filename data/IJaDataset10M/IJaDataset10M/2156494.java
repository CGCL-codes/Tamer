package Controle;

import util.Constantes;
import Jplay.GameImage;
import Jplay.Mouse;
import MotorGrafico.InterfaceTela;
import MotorGrafico.MotorGrafico;

/**
 * Classe respons�vel por apresentar o menu inicial.
 * 
 * @author Gefersom C. Lima
 */
public class TelaMenuInicial implements InterfaceTela {

    GameImage fundo;

    GameImage botaoJogar;

    GameImage botaoCreditos;

    GameImage botaoRegras;

    /**
	 * Usado para carregar os objetos na mem�ria principal.
	 */
    @Override
    public void carregar() {
        fundo = new GameImage(Constantes.IMG_MENU_INICIAL_FUNDO);
        botaoJogar = new GameImage(Constantes.IMG_MENU_INICIAL_BOTAO_JOGAR);
        botaoCreditos = new GameImage(Constantes.IMG_MENU_INICIAL_BOTAO_CREDITOS);
        botaoRegras = new GameImage(Constantes.IMG_MENU_INICIAL_BOTAO_REGRAS);
        botaoJogar.setPosition(320, 360);
        botaoCreditos.setPosition(500, 400);
        botaoRegras.setPosition(100, 400);
    }

    /**
	 * Usado para descarregar os objetos criados da mem�ria.
	 */
    @Override
    public void descarregar() {
        fundo = null;
    }

    /**
	 * Respons�vel por controlar a l�gica inerente a classe corrente.
	 */
    @Override
    public void logica() {
    }

    /**
	 * Respons�vel por desenhar as imagens usadas.
	 */
    @Override
    public void desenhar() {
        fundo.draw();
        botaoJogar.draw();
        botaoCreditos.draw();
        botaoRegras.draw();
    }

    /**
	 * Seta a pr�xima tela a ser executada.
	 */
    @Override
    public void proxTela() {
        Mouse mouse = MotorGrafico.getInstancia().getJanela().getMouse();
        if (mouse.isOverObject(botaoCreditos) && mouse.isLeftButtonPressed()) {
            MotorGrafico.getInstancia().setProxTela(Constantes.TELA_CREDITOS);
        }
        if (mouse.isOverObject(botaoJogar) && mouse.isLeftButtonPressed()) {
            MotorGrafico.getInstancia().setProxTela(Constantes.TELA_JOGAR);
        }
    }
}
