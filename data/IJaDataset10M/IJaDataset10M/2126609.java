package info.walnutstreet.vs.ps03v2.common;

import java.io.Serializable;

/**
 * @author Christoph Gostner
 * @version 0.9
 *
 */
public class CommandData implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -379462986195191511L;

    private Command command;

    private Integer id;

    private Integer number;

    /**
	 * Constructor.
	 * 
	 * @param command The command to set.
	 */
    private CommandData(Command command) {
        super();
        this.command = command;
    }

    /**
	 * Get the command to retrieve the good list.
	 * 
	 * @return The command to retrieve the good list.
	 */
    public static CommandData getGoodList() {
        return new CommandData(Command.CLIENT_SENDS_REQUEST_FOR_GOOD_LIST);
    }

    /**
	 * Command to reserve a good.
	 * 
	 * @param id The good's id. 
	 * @param number The number of goods.
	 * @return Command to reserve a good.
	 */
    public static CommandData getReserveGood(int id, int number) {
        CommandData data = new CommandData(Command.CLIENT_SENDS_REQUEST_RESERVE_GOOD);
        data.setId(id);
        data.setNumber(number);
        return data;
    }

    /**
	 * Command to edit a reservation.
	 * 
	 * @param id The good's id.
	 * @param num The number of goods to set.
	 * @return Command to edit a reservation.
	 */
    public static CommandData getEditReserveGood(int id, int num) {
        CommandData data = new CommandData(Command.CLIENT_SENDS_REQUEST_EDIT_RESERVE_GOOD);
        data.setId(id);
        data.setNumber(num);
        return data;
    }

    /**
	 * Command to delete a reservation.
	 * 
	 * @param id The id of the good.
	 * @return Command to delete a reservation.
	 */
    public static CommandData getDeleteReserveGood(int id) {
        CommandData data = new CommandData(Command.CLIENT_SENDS_REQUEST_DELETE_RESERVE_GOOD);
        data.setId(id);
        return data;
    }

    /**
	 * Command to buy all the goods in the cart.
	 * 
	 * @return Command to buy all the goods in the cart.
	 */
    public static CommandData getBuyMyCart() {
        return new CommandData(Command.CLIENT_SENDS_REQUEST_BUY_MY_CART);
    }

    /**
	 * @return the command
	 */
    public Command getCommand() {
        return command;
    }

    /**
	 * @param command the command to set
	 */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
	 * @return the id
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @return the number
	 */
    public Integer getNumber() {
        return number;
    }

    /**
	 * @param number the number to set
	 */
    public void setNumber(Integer number) {
        this.number = number;
    }
}
