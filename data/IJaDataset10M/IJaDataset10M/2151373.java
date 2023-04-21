package game;

public class Database {

    /**
	 * La Base de données / Voc
	 * 
	 * @author SENG Daniel
	 *
	 */
    public Database() {
        setLanguage(game.Vocab.language.FR);
    }

    public void setLanguage(int l) {
        new Vocab(l);
    }
}
