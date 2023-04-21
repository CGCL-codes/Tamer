package blueprint4j.gui;

import java.sql.*;
import blueprint4j.utils.*;
import javax.swing.*;

public interface JoinPanelJoiner {

    /**
	* Returns all the possible destination Entities
	*/
    public Bindable getDestinations();

    /**
	* Returns a join entity for a particular destination entity NULL for not joined
	*/
    public Bindable getJoin(Bindable source, Bindable destination);

    /**
	* Create a new join entity
	*/
    public Bindable createNewJoin(Bindable source, Bindable destination) throws BindException;

    /**
	* Remove a join
	*/
    public void removeJoin(Binder binder, Bindable join) throws BindException;
}
