package edu.diseno.jaspict3d.render;

/**
 * Representa el contrato que deben seguir todos los motores de dibujo en Jaspict.
 * @author Martin Uzquiano
 */
public interface JaspictRenderer {

    /**
	 * Lanza la creacion de la escena en el motor instanciado. En este metodo se deberia lanzar la iniciacion propia de cada motor
	 * a traves del metodo init();
	 * Aqui ya se deberia poder visualizar la escena, ya sea mediante un Canvas o JFrame.
	 * @param GraphicScene scene: representa la escena modelo wrapeada con  funcionalidad visual. 
	 */
    public void start(final GraphicScene scene);

    /**
	 * Inicializa los detallas propios de cada motor.
	 */
    public void init();
}
