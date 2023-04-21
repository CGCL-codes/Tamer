package org.fudaa.ctulu;

/**
 * une interface pour notifier la progression d'une tache. Doit �tre utilis�e dans
 * la tache, et appel�e r�guli�rement. La tache peut notifier certains d�tails.
 *
 * @author marchand@deltacad.fr
 * @version $Id: ProgressionDetailedInterface.java 6944 2012-02-02 08:53:00Z chrisc83 $
 */
public interface ProgressionDetailedInterface extends ProgressionInterface {

    /**
   * Ajoute un d�tail aux d�tails pr�c�dents. La string peut comporter des retours
   * chariots pour passer � la ligne. La methode {@link #reset()} efface les
   * details.
   * 
   * @param _s La string.
   */
    public void appendDetail(String _s);

    /**
   * Ajoute un d�tail aux d�tails pr�c�dents, en passant � la ligne en fin.
   * 
   * @param _s La string.
   */
    public void appendDetailln(String _s);
}
