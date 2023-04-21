package main;

import objetos.Citta.Grama;
import objetos.Citta.Objeto;
import org.jouvieje.math.Vector3f;
import Banco.Evento;
import Banco.HistoricoEventos;

/**
 * Classe responsavel por verificar se eh posivel construir ou deletar alguma
 * edificacao
 * 
 * @author LELIC
 * 
 */
public class Builder {

    private Objeto[][] mapa;

    private Objeto objeto;

    private int iX;

    private int iZ;

    private int objectID;

    boolean constroiOK = false;

    public HistoricoEventos he;

    /**
	 * Atualiza o mapa e recebe as posicoes do objeto
	 * 
	 * @param mapa
	 * @param indexX
	 * @param indexZ
	 * @return retorma o mapa com o novo objeto inserido
	 */
    public Objeto[][] updateMapa(Objeto mapa[][], int indexX, int indexZ) {
        this.mapa = mapa;
        iX = indexX;
        iZ = indexZ;
        return this.mapa;
    }

    /**
	 * Faz a Construcao do objeto
	 */
    public boolean construirEdificacao() {
        int tamX = objeto.getTamanhoX() / 2, tamZ = objeto.getTamanhoZ() / 2;
        if (verificarTerreno(tamX, tamZ)) {
            objectID = mapa[iX][iZ].getID();
            for (int i = iX - tamX; i < iX + tamX; i++) {
                for (int z = iZ - tamZ; z < iZ + tamZ; z++) {
                    mapa[i][z].setPessoas(objeto.getPessoas());
                    mapa[i][z].setLixo(objeto.getLixo());
                    mapa[i][z].setAgua(objeto.getAgua());
                    mapa[i][z].setAlimento(objeto.getAlimento());
                    mapa[i][z].setEsgoto(objeto.getEsgoto());
                    mapa[i][z].setEnergia(objeto.getEnergia());
                    mapa[i][z].setTextura(objeto.getTextura());
                    mapa[i][z].setNome(objeto.getNome() + ".");
                    mapa[i][z].setIDPai(objectID);
                    mapa[i][z].setCor(new Vector3f(0, 0, 0));
                    he.adicionaEvento(new Evento(Temporizador.getDia(), objectID, i, z, 0, ""));
                    System.out.println(String.valueOf(i) + ' ' + String.valueOf(z) + ' ' + String.valueOf(objeto.getNome()));
                }
            }
            Vector3f pos = new Vector3f(mapa[iX][iZ].getPosX(), mapa[iX][iZ].getPosY(), mapa[iX][iZ].getPosZ());
            objeto.setPosX((int) pos.getX());
            objeto.setPosY(pos.getY());
            objeto.setPosZ((int) pos.getZ());
            objeto.setID(objectID);
            objeto.setCor(new Vector3f(0, 0, 0));
            this.mapa[iX][iZ] = objeto;
            updateMapa(mapa, iX, iZ);
            return true;
        } else return false;
    }

    /**
	 * Constroi a Rua em duas etapas: 1 - Percorre o vetor do mapa em Z e X
	 * setando o nome para Rua 2 - Analisa o tipo de juncao de cada rua
	 * construida e seta a textura
	 * 
	 * @param pIX
	 *            ponto Inicial X
	 * @param pIZ
	 *            ponto Inicial Z
	 * @param pFX
	 *            ponto Final X
	 * @param pFZ
	 *            ponto Final Z
	 * @param txt
	 *            indice da textura
	 * @return retorno true se construiu
	 */
    public boolean construirRua(int pIX, int pIZ, int pFX, int pFZ, int txt) {
        for (int i = pIX; i < pFX; i++) mapa[pIZ][i].setNome("Rua");
        for (int i = pIX; i > pFX; i--) mapa[pIZ][i].setNome("Rua");
        for (int z = pIZ; z >= pFZ; z--) mapa[z][pFX].setNome("Rua");
        for (int z = pIZ; z <= pFZ; z++) mapa[z][pFX].setNome("Rua");
        for (int i = pIX; i < pFX; i++) mapa[pIZ][i].setTextura(analisarRua(pIZ, i, txt));
        for (int i = pIX; i > pFX; i--) mapa[pIZ][i].setTextura(analisarRua(pIZ, i, txt));
        for (int z = pIZ; z >= pFZ; z--) mapa[z][pFX].setTextura(analisarRua(z, pFX, txt));
        for (int z = pIZ; z <= pFZ; z++) mapa[z][pFX].setTextura(analisarRua(z, pFX, txt));
        return true;
    }

    /**
	 * Exclui Edificacoes e Ruas Para Edificacoes faz o teste do ID do TILE do
	 * Pai
	 */
    public void excluirEdificacao() {
        if (mapa[iX][iZ].getIDPai() == 0) {
            objectID = mapa[iX][iZ].getID();
            for (short i = 0; i < 100; i++) {
                for (short x = 0; x < 100; x++) {
                    if (mapa[i][x].getIDPai() == objectID) {
                        mapa[i][x] = new Grama(mapa[i][x].getPosX(), (int) mapa[i][x].getPosY(), mapa[i][x].getPosZ(), mapa[i][x].getID());
                    }
                }
            }
        } else {
            objectID = mapa[iX][iZ].getIDPai();
            for (short i = 0; i < 100; i++) {
                for (short x = 0; x < 100; x++) {
                    if (mapa[i][x].getIDPai() == objectID || mapa[i][x].getID() == objectID) {
                        mapa[i][x] = new Grama(mapa[i][x].getPosX(), (int) mapa[i][x].getPosY(), mapa[i][x].getPosZ(), mapa[i][x].getID());
                    }
                }
            }
        }
        mapa[iX][iZ] = new Grama(mapa[iX][iZ].getPosX(), (int) mapa[iX][iZ].getPosY(), mapa[iX][iZ].getPosZ(), mapa[iX][iZ].getID());
        updateMapa(mapa, iX, iZ);
    }

    /***
	 * Verifica se os Tiles onde se quer construir � plano e � uma grama
	 * 
	 * @return retorne true se d� para construir
	 */
    public boolean verificarTerreno(int xTam, int zTam) {
        float altura = mapa[iX][iZ].getPosY();
        for (int i = iX - xTam; i < iX + xTam; i++) {
            for (int j = iZ - zTam; j < iZ + zTam; j++) {
                if ((i < 100 && i >= 0) && (j < 100 && j >= 0)) if ((mapa[i][j].getNome().equals("Grama")) || (mapa[i][j].getNome().equals("Areia"))) constroiOK = true; else return constroiOK = false; else return constroiOK = false;
            }
        }
        for (int i = iX - xTam - 1; i < iX + xTam + 1; i++) {
            for (int j = iZ - zTam - 1; j < iZ + zTam + 1; j++) {
                if ((i < 100 && i >= 0) && (j < 100 && j >= 0)) {
                    if ((mapa[i][j].getPosY() >= altura + 0.3f) || (mapa[i][j].getPosY() <= altura - 0.3f)) return constroiOK = false; else constroiOK = true;
                } else return constroiOK = false;
            }
        }
        return constroiOK;
    }

    /**
	 * Verifica se os Tiles onde se quer excluir nao eh areia, agua ou grama
	 * 
	 * @return
	 */
    public boolean verificarExclusaoObjeto() {
        if (mapa[iX][iZ].getNome().equals("Agua") || mapa[iX][iZ].getNome().equals("Grama") || mapa[iX][iZ].getNome().equals("Areia")) {
            System.out.println("NAO EXCLUI");
            return false;
        } else {
            System.out.println("Exclui");
            return true;
        }
    }

    /**
	 * Verifica se o Tile onde se quer construir uma rua naum � uma edificacao
	 * ou Lago
	 * 
	 * @param pIX
	 *            ponto Inicial X
	 * @param pIZ
	 *            ponto Inicial Z
	 * @param pFX
	 *            ponto Final X
	 * @param pFZ
	 *            ponto Final Z
	 * @param caso
	 *            se 1 para tratar mais de um TILE
	 * @return retorna true se d� para construir
	 */
    public boolean verificarRua(int pIX, int pIZ, int pFX, int pFZ, int caso) {
        boolean constroiOK = true;
        switch(caso) {
            case 1:
                for (int i = pIX; i < pFX; i++) if ((!mapa[pIZ][i].getNome().equals("Grama")) && (!mapa[pIZ][i].getNome().equals("Areia")) && (!mapa[pIZ][i].getNome().equals("Rua"))) constroiOK = false;
                for (int i = pIX; i > pFX; i--) if ((!mapa[pIZ][i].getNome().equals("Grama")) && (!mapa[pIZ][i].getNome().equals("Areia")) && (!mapa[pIZ][i].getNome().equals("Rua"))) constroiOK = false;
                for (int z = pIZ; z >= pFZ; z--) if ((!mapa[z][pFX].getNome().equals("Grama")) && (!mapa[z][pFX].getNome().equals("Areia")) && (!mapa[z][pFX].getNome().equals("Rua"))) constroiOK = false;
                for (int z = pIZ; z <= pFZ; z++) if ((!mapa[z][pFX].getNome().equals("Grama")) && (!mapa[z][pFX].getNome().equals("Areia")) && (!mapa[z][pFX].getNome().equals("Rua"))) constroiOK = false;
                break;
            case 2:
                if ((mapa[iX][iZ].getNome().equals("Grama")) || (mapa[iX][iZ].getNome().equals("Areia")) || (mapa[iX][iZ].getNome().equals("Rua"))) constroiOK = true; else constroiOK = false;
                break;
        }
        if (iX == 99 || iX == 0 || iZ == 99 || iZ == 0 || pIX == 99 || pIX == 0 || pIZ == 99 || pIZ == 0) return constroiOK = false;
        return constroiOK;
    }

    /**
	 * Recebe qual tipo de objeto a ser construido
	 * 
	 * @param objeto
	 *            (Casas, Predio, Hospital, etc)
	 */
    public void setConstrucao(Objeto objeto) {
        this.objeto = objeto;
    }

    /**
	 * Analisa o tipo de juncao de cada rua construida e seta a textura
	 * 
	 * @param x
	 *            indice X do mappa
	 * @param z
	 *            indice X do mappa
	 * @param txt
	 *            indice da Textura
	 * @return o indice da Textura naquele TILE
	 */
    private int analisarRua(int x, int z, int txt) {
        try {
            if (mapa[x + 1][z].getNome().equals("Rua") && mapa[x - 1][z].getNome().equals("Rua") && mapa[x][z + 1].getNome().equals("Rua")) return txt - 1;
            if (mapa[x + 1][z].getNome().equals("Rua") && mapa[x - 1][z].getNome().equals("Rua") && mapa[x][z - 1].getNome().equals("Rua")) return txt - 1;
            if (mapa[x][z + 1].getNome().equals("Rua") && mapa[x - 1][z].getNome().equals("Rua") && mapa[x][z - 1].getNome().equals("Rua")) return txt - 1;
            if (mapa[x][z + 1].getNome().equals("Rua") && mapa[x + 1][z].getNome().equals("Rua") && mapa[x][z - 1].getNome().equals("Rua")) return txt - 1;
            if (mapa[x + 1][z].getNome().equals("Rua") && mapa[x][z + 1].getNome().equals("Rua")) return txt + 4;
            if (mapa[x - 1][z].getNome().equals("Rua") && mapa[x][z + 1].getNome().equals("Rua")) return txt + 3;
            if (mapa[x + 1][z].getNome().equals("Rua") && mapa[x][z - 1].getNome().equals("Rua")) return txt + 5;
            if (mapa[x - 1][z].getNome().equals("Rua") && mapa[x][z - 1].getNome().equals("Rua")) return txt + 1;
            if (mapa[x - 1][z].getNome().equals("Rua") && mapa[x + 1][z].getNome().equals("Rua")) return txt;
            if (mapa[x][z + 1].getNome().equals("Rua") || mapa[x][z - 1].getNome().equals("Rua")) return txt + 2;
            return txt;
        } catch (Exception e) {
            return txt;
        }
    }
}
