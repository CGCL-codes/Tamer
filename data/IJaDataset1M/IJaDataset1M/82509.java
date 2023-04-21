package fr.free.jchecs.swg;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import fr.free.jchecs.core.Game;
import static fr.free.jchecs.swg.ResourceUtils.getI18NString;
import static fr.free.jchecs.swg.ResourceUtils.getImageIcon;

/**
 * Composant graphique permettant de se déplacer dans l'enregistrement d'une
 * partie.
 * 
 * @author David Cotton
 */
final class Recorder {

    /** Icône "premier". */
    private static final Icon FIRST_ICON = getImageIcon("first.png");

    /** Icône "dernier". */
    private static final Icon LAST_ICON = getImageIcon("last.png");

    /** Icône "suivant". */
    private static final Icon NEXT_ICON = getImageIcon("next.png");

    /** Icône "précédent". */
    private static final Icon PREVIOUS_ICON = getImageIcon("previous.png");

    /** Marge par défaut. */
    private static final Insets DEFAULT_INSETS = new Insets(0, 0, 0, 0);

    /** Composant graphique externe. */
    private final JComponent _component;

    /**
	 * Instancie un nouveau composant.
	 * 
	 * @param pPartie
	 *            Définition de partie liée au composant.
	 */
    Recorder(final Game pPartie) {
        assert pPartie != null;
        final JPanel fond = new JPanel(new GridLayout(1, 4));
        final Game partie = pPartie;
        final JButton premier = new JButton(FIRST_ICON);
        premier.setEnabled(pPartie.getCurrentMoveIndex() >= 0);
        premier.setToolTipText(getI18NString("button.first.tooltip"));
        premier.setMargin(DEFAULT_INSETS);
        fond.add(premier);
        final JButton precedent = new JButton(PREVIOUS_ICON);
        precedent.setEnabled(pPartie.getCurrentMoveIndex() >= 0);
        precedent.setToolTipText(getI18NString("button.previous.tooltip"));
        precedent.setMargin(DEFAULT_INSETS);
        fond.add(precedent);
        final JButton suivant = new JButton(NEXT_ICON);
        suivant.setEnabled(pPartie.getCurrentMoveIndex() < pPartie.getMovesCount() - 1);
        suivant.setToolTipText(getI18NString("button.next.tooltip"));
        suivant.setMargin(DEFAULT_INSETS);
        fond.add(suivant);
        final JButton dernier = new JButton(LAST_ICON);
        dernier.setEnabled(pPartie.getCurrentMoveIndex() < pPartie.getMovesCount() - 1);
        dernier.setToolTipText(getI18NString("button.last.tooltip"));
        dernier.setMargin(DEFAULT_INSETS);
        fond.add(dernier);
        _component = fond;
        final RecorderListener ecouteur = new RecorderListener(pPartie, premier, precedent, suivant, dernier);
        premier.addActionListener(ecouteur);
        precedent.addActionListener(ecouteur);
        suivant.addActionListener(ecouteur);
        dernier.addActionListener(ecouteur);
        partie.addPropertyChangeListener("position", ecouteur);
    }

    /**
	 * Renvoi le composant graphique affichable.
	 * 
	 * @return Composant graphique.
	 */
    JComponent getComponent() {
        assert _component != null;
        return _component;
    }

    /**
	 * Classe à l'écoute des actions du "Recorder".
	 */
    private static final class RecorderListener implements ActionListener, PropertyChangeListener {

        /** Composant permettant d'aller au premier. */
        private final JComponent _first;

        /** Référence de la partie liée. */
        private final Game _game;

        /** Composant permettant d'aller au dernier. */
        private final JComponent _last;

        /** Composant permettant d'aller au suivant. */
        private final JComponent _next;

        /** Composant permettant d'aller au précédent. */
        private final JComponent _previous;

        /**
		 * Instancie un nouvel écouteur.
		 * 
		 * @param pGame
		 *            Partie liée.
		 * @param pFirst
		 *            Composant permettant d'aller au début.
		 * @param pPrevious
		 *            Composant permettant d'aller au précédent.
		 * @param pNext
		 *            Composant permettant d'aller au suivant.
		 * @param pLast
		 *            Composant permettant d'aller à la fin.
		 */
        RecorderListener(final Game pGame, final JComponent pFirst, final JComponent pPrevious, final JComponent pNext, final JComponent pLast) {
            assert pGame != null;
            assert pFirst != null;
            assert pPrevious != null;
            assert pNext != null;
            assert pLast != null;
            _game = pGame;
            _first = pFirst;
            _previous = pPrevious;
            _next = pNext;
            _last = pLast;
        }

        /**
		 * Réagit au déclenchement d'un bouton.
		 * 
		 * @param pEvt
		 *            Evènement signalant le déclenchement.
		 */
        @Override
        public void actionPerformed(final ActionEvent pEvt) {
            assert pEvt != null;
            final Object src = pEvt.getSource();
            if (src == _first) {
                _game.goFirst();
            } else if (src == _previous) {
                _game.goPrevious();
            } else if (src == _next) {
                _game.goNext();
            } else if (src == _last) {
                _game.goLast();
            } else {
                assert false;
            }
        }

        /**
		 * Réagit au changement dans la définition de la partie.
		 * 
		 * @param pEvt
		 *            Evènement signalant le changement.
		 */
        @Override
        public void propertyChange(final PropertyChangeEvent pEvt) {
            assert pEvt != null;
            _first.setEnabled(_game.getCurrentMoveIndex() >= 0);
            _previous.setEnabled(_game.getCurrentMoveIndex() >= 0);
            _next.setEnabled(_game.getCurrentMoveIndex() < _game.getMovesCount() - 1);
            _last.setEnabled(_game.getCurrentMoveIndex() < _game.getMovesCount() - 1);
        }
    }
}
