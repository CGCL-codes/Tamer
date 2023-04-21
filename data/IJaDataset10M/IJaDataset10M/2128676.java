package clickandlearn.conquis.multiplayer;

import java.util.ArrayList;
import org.newdawn.slick.state.GameState;
import clickandlearn.conquis.cargador.Factoria;
import clickandlearn.conquis.singleplayer.GameStates;

public class FactoriaRed implements Factoria {

    private final int MAX_JUEGOS = 10;

    private ArrayList<GameState>[][] estadosRed;

    public FactoriaRed() {
        this.estadosRed = new ArrayList[3][MAX_JUEGOS];
        for (int i = 0; i < this.estadosRed.length; i++) {
            for (int j = 0; j < this.estadosRed[i].length; j++) {
                this.estadosRed[i][j] = null;
            }
        }
        this.anadeJuegosUnJugador();
        this.anadeJuegosDosJugadores();
        this.anadeJuegosPiratas();
    }

    private void anadeJuegosUnJugador() {
        this.estadosRed[0][0] = new ArrayList<GameState>();
        this.estadosRed[0][0].add(new clickandlearn.conquis.minijuegos.relaciones.MainMenuState(GameStates.RELACIONESMENU));
        this.estadosRed[0][0].add(new clickandlearn.conquis.minijuegos.relaciones.red.InGameState(GameStates.RELACIONES));
        this.estadosRed[0][0].add(new clickandlearn.conquis.minijuegos.relaciones.PuntuacionState(GameStates.RELACIONESPUNTUACION));
        this.estadosRed[0][1] = new ArrayList<GameState>();
        this.estadosRed[0][1].add(new clickandlearn.conquis.minijuegos.estrellas.MainMenuState(GameStates.ESTRELLASMENU));
        this.estadosRed[0][1].add(new clickandlearn.conquis.minijuegos.estrellas.InstructionState(GameStates.ESTRELLASINSTRUCTIONS));
        this.estadosRed[0][1].add(new clickandlearn.conquis.minijuegos.estrellas.red.InGameState(GameStates.ESTRELLAS));
        this.estadosRed[0][1].add(new clickandlearn.conquis.minijuegos.estrellas.PuntuacionState(GameStates.ESTRELLASPUNTUACION));
        this.estadosRed[0][2] = new ArrayList<GameState>();
        this.estadosRed[0][2].add(new clickandlearn.conquis.minijuegos.rutanavegacion.Introduccion(GameStates.RUTAINTRODUCCION));
        this.estadosRed[0][2].add(new clickandlearn.conquis.minijuegos.rutanavegacion.MainMenuState(GameStates.RUTAMENU));
        this.estadosRed[0][2].add(new clickandlearn.conquis.minijuegos.rutanavegacion.red.InGameState(GameStates.RUTA));
        this.estadosRed[0][2].add(new clickandlearn.conquis.minijuegos.rutanavegacion.red.CuestionFacilState(GameStates.RUTACUESTIONFACIL));
        this.estadosRed[0][2].add(new clickandlearn.conquis.minijuegos.rutanavegacion.red.CuestionDificilState(GameStates.RUTACUESTIONDIFICIL));
        this.estadosRed[0][2].add(new clickandlearn.conquis.minijuegos.rutanavegacion.RespuestaAcertadaState(GameStates.RUTARESPUESTAACERTADA));
        this.estadosRed[0][2].add(new clickandlearn.conquis.minijuegos.rutanavegacion.RespuestaNoAcertadaState(GameStates.RUTARESPUESTANOACERTADA));
        this.estadosRed[0][3] = new ArrayList<GameState>();
        this.estadosRed[0][3].add(new clickandlearn.conquis.minijuegos.ciudades.MainMenuState(GameStates.CIUDADESMENU));
        this.estadosRed[0][3].add(new clickandlearn.conquis.minijuegos.ciudades.InstruccionesState(GameStates.CIUDADESINSTRUCCIONES));
        this.estadosRed[0][3].add(new clickandlearn.conquis.minijuegos.ciudades.red.InGameState(GameStates.CIUDADES));
        this.estadosRed[0][3].add(new clickandlearn.conquis.minijuegos.ciudades.PuntuacionState(GameStates.CIUDADESPUNTUACION));
        this.estadosRed[0][4] = new ArrayList<GameState>();
        this.estadosRed[0][4].add(new clickandlearn.conquis.minijuegos.palabras.MainMenuState(GameStates.PALABRASMENU));
        this.estadosRed[0][4].add(new clickandlearn.conquis.minijuegos.palabras.InstructionState(GameStates.PALABRASINSTRUCTIONS));
        this.estadosRed[0][4].add(new clickandlearn.conquis.minijuegos.palabras.red.InGameState(GameStates.PALABRAS));
        this.estadosRed[0][4].add(new clickandlearn.conquis.minijuegos.palabras.PuntuacionState(GameStates.PALABRASPUNTUACION));
        this.estadosRed[0][5] = new ArrayList<GameState>();
        this.estadosRed[0][5].add(new clickandlearn.conquis.minijuegos.mecanografia.MainMenuState(GameStates.MECANOGRAFIAMENU));
        this.estadosRed[0][5].add(new clickandlearn.conquis.minijuegos.mecanografia.red.InGameState(GameStates.MECANOGRAFIA));
        this.estadosRed[0][5].add(new clickandlearn.conquis.minijuegos.mecanografia.PuntuacionState(GameStates.MECANOGRAFIAPUNTUACION));
        this.estadosRed[0][6] = new ArrayList<GameState>();
        this.estadosRed[0][6].add(new clickandlearn.conquis.minijuegos.trivial.MainMenuState(GameStates.TRIVIALMENU));
        this.estadosRed[0][6].add(new clickandlearn.conquis.minijuegos.trivial.red.InGameState(GameStates.TRIVIAL));
        this.estadosRed[0][6].add(new clickandlearn.conquis.minijuegos.trivial.GameOverState(GameStates.TRIVIALGAMEOVER));
        this.estadosRed[0][7] = new ArrayList<GameState>();
        this.estadosRed[0][7].add(new clickandlearn.conquis.minijuegos.puzzle.MainMenuState(GameStates.PUZZLEMENU));
        this.estadosRed[0][7].add(new clickandlearn.conquis.minijuegos.puzzle.PantallaI(GameStates.PUZZLEINSTRUCCIONES));
        this.estadosRed[0][7].add(new clickandlearn.conquis.minijuegos.puzzle.red.InGameState(GameStates.PUZZLE));
        this.estadosRed[0][7].add(new clickandlearn.conquis.minijuegos.puzzle.PuntuacionState(GameStates.PUZZLEPUNTUACION));
    }

    private void anadeJuegosDosJugadores() {
        this.estadosRed[1][0] = new ArrayList<GameState>();
        this.estadosRed[1][0].add(new clickandlearn.conquis.minijuegos.barcos.MainMenuState(GameStates.BARCOSMENU));
        this.estadosRed[1][0].add(new clickandlearn.conquis.minijuegos.barcos.red.InGameState(GameStates.BARCOS));
        this.estadosRed[1][1] = new ArrayList<GameState>();
        this.estadosRed[1][1].add(new clickandlearn.conquis.minijuegos.pasarela.MainMenuState(GameStates.PASARELAMENU));
        this.estadosRed[1][1].add(new clickandlearn.conquis.minijuegos.pasarela.red.InGameState(GameStates.PASARELA));
    }

    private void anadeJuegosPiratas() {
        this.estadosRed[2][0] = new ArrayList<GameState>();
        this.estadosRed[2][0].add(new clickandlearn.conquis.minijuegos.retonaipe.Introduction(GameStates.NAIPEINTRODUCCION));
        this.estadosRed[2][0].add(new clickandlearn.conquis.minijuegos.retonaipe.InstructionState(GameStates.NAIPEINSTRUCCIONES));
        this.estadosRed[2][0].add(new clickandlearn.conquis.minijuegos.retonaipe.red.InGameState(GameStates.NAIPEJUEGO));
        this.estadosRed[2][1] = new ArrayList<GameState>();
        this.estadosRed[2][1].add(new clickandlearn.conquis.minijuegos.piratas.TituloPirata(GameStates.PIRATASMENU));
        this.estadosRed[2][1].add(new clickandlearn.conquis.minijuegos.piratas.InstruccionesPirata(GameStates.PIRATASINSTRUCCIONES));
        this.estadosRed[2][1].add(new clickandlearn.conquis.minijuegos.piratas.red.InGameState(GameStates.PIRATAS));
    }

    public ArrayList<GameState>[][] getJuegos() {
        return this.estadosRed;
    }
}
