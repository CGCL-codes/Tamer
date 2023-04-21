package net.sf.breed.kout.io;

/**
 * Capsules all input coming from the user.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since Nov 5, 2006
 */
public interface IBreedUserInputListener {

    /**
   * Indicates whether given key is currently pressed.
   * 
   * @param keyCode The key code.
   * @return Whether given key is currently pressed.
   */
    public boolean isPressed(int keyCode);
}
