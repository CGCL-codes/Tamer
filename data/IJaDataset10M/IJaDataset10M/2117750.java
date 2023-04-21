package tikara.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import tikara.events.ActivateCouncil;
import tikara.events.NewCouncil;
import tikara.events.NewTikaraModel;
import tikara.events.RemoveCouncil;
import tikara.gui.utilities.English;
import tikara.gui.utilities.Language;

/**
 * 
    Copyright (c) 2008 by Serge Haenni
    
    This file is part of Tikara.

    Tikara is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Tikara is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Tikara.  If not, see <http://www.gnu.org/licenses/>.
    
 * @author Serge Haenni
 *
 */
public class TikaraModel extends Observable implements Serializable, Observer {

    /**
	 * serial uid
	 */
    private static final long serialVersionUID = -2690522854469544455L;

    /**
	 * The active council, which will be shown in all display modes
	 */
    private Council activeCouncil;

    /**
	 * All user known councils
	 */
    private LinkedList<Council> councils;

    private FormalDebateSnapshot formalDebate;

    private CaucusSnapshot caucus;

    private UnmoderatedCaucusSnapshot unmoderatedCaucus;

    /**
	 * Constructor, initialize local variables
	 */
    public TikaraModel() {
        councils = new LinkedList<Council>();
        formalDebate = new FormalDebateSnapshot();
        caucus = new CaucusSnapshot();
        unmoderatedCaucus = new UnmoderatedCaucusSnapshot();
        setActiveCouncil(new Council(""));
    }

    /**
	 * Setter for activeCouncil
	 */
    public void setActiveCouncil(Council activeCouncil) {
        Council oldActiveCouncil = this.activeCouncil;
        this.activeCouncil = activeCouncil;
        setChanged();
        notifyObservers(new ActivateCouncil(oldActiveCouncil, activeCouncil));
    }

    public void setActiveCouncil(String name) {
        Council c = getCouncilByName(name);
        setActiveCouncil(c);
    }

    /**
	 * Getter for activeCouncil
	 */
    public Council getActiveCouncil() {
        if (activeCouncil == null && getCouncils().size() > 0) setActiveCouncil(getCouncils().getFirst());
        if (activeCouncil == null) {
            setActiveCouncil(new Council(""));
        }
        return activeCouncil;
    }

    /**
	 * Returns all councils
	 */
    public LinkedList<Council> getCouncils() {
        return councils;
    }

    /**
	 * Adds a council
	 */
    public void addCouncil(Council c) {
        councils.add(c);
        c.addObserver(this);
        setChanged();
        notifyObservers(new NewCouncil(c));
    }

    /**
	 * Removes a council
	 * @param name the name of the council to be removed
	 */
    public void removeCouncil(String name) {
        Council c = getCouncilByName(name);
        if (activeCouncil.equals(c)) {
            activeCouncil = new Council("");
            setChanged();
            notifyObservers(new ActivateCouncil(c, activeCouncil));
        }
        councils.remove(c);
        setChanged();
        notifyObservers(new RemoveCouncil(c));
    }

    /**
	 * Changes the name of a council (identified by its old name) to a new name
	 */
    public void changeCouncilName(String oldName, String newName) {
        Council c = getCouncilByName(oldName);
        if (c != null) {
            c.setName(newName);
            setChanged();
            notifyObservers();
        }
    }

    /**
	 * Gets the corresponding 'Council' object to a given council name
	 */
    public Council getCouncilByName(String name) {
        for (Council o : getCouncils()) {
            if (o.getName().equals(name)) {
                return o;
            }
        }
        return null;
    }

    /**
	 * update method, gets called from all observable objects if they change
	 */
    public void update(Observable obs, Object obj) {
        setChanged();
        notifyObservers(obj);
    }

    /**
	 * sets me setChanged(), mainly used if you open me, to tell all that the model
	 * has changed
	 */
    public void reinit() {
        for (Council c : councils) {
            c.addObserver(this);
            c.reinit();
        }
        setChanged();
        notifyObservers(new NewTikaraModel());
    }

    public CaucusSnapshot getCaucus() {
        return caucus;
    }

    public FormalDebateSnapshot getFormalDebate() {
        return formalDebate;
    }

    public UnmoderatedCaucusSnapshot getUnmoderatedCaucus() {
        return unmoderatedCaucus;
    }

    private Language currentLanguage = null;

    public Language getCurrentLanguage() {
        if (currentLanguage == null) return new English(); else return currentLanguage;
    }
}
