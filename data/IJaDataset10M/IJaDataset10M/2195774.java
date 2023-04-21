package Model;

/**
 * Factory do zumbi de velocidade media
 * 
 * @author Edjane Catolle, Igor Henrique, Luiz Augusto, Vladmir Chicarolli
 * @version 1.0.0
 * @since 05/31/2011
 */
public class ZombieFatFactory implements ZombieFactory {

    @Override
    public ZombieModel CreateZombie() {
        return new ZombieFat();
    }
}
