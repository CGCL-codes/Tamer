package models;

import java.util.Date;
import siena.Column;
import siena.Generator;
import siena.Id;
import siena.Model;
import siena.NotNull;

/**
 * Abstract class to be extented by any type of Event.
 * 
 * @author slever
 * 
 */
public class Event extends Model {

    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    /**
	 * @uml.property name="dateTime"
	 */
    @Column("date")
    @NotNull
    public Date date;

    /**
	 */
    public Event(Date date) {
        this.date = date;
    }

    @Override
    public void insert() {
        super.insert();
    }

    @Override
    public void update() {
        super.update();
    }
}
