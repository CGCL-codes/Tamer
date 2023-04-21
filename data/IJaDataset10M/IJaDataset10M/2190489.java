package rap;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A class to save a RoomObject. In the RoomObject class is saved the quantity
 * of the differen objects, available in an specific room
 * @author Markus Bellgardt
 */
@Entity
public class RoomObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int q_pc = 0;

    private int q_sldstn = 0;

    private int q_beamer = 0;

    private int q_board = 0;

    /**
     * default constructor for the class RoomObject
     */
    public RoomObject() {
    }

    /**
     * this method gives a room 
     * all possible objects(pc, beamer, board, soldering station) 
     * @param q_pc
     * stands for the number of pcs a room should have
     * @param q_sldstn
     * stands for the number of soldering stations a room should have
     * @param q_beamer
     * stands for the number of beamers a room should have
     * @param q_board
     * stands for the number of boards a room should have
     */
    public void setAll(int q_pc, int q_sldstn, int q_beamer, int q_board) {
        this.q_beamer = q_beamer;
        this.q_board = q_board;
        this.q_pc = q_pc;
        this.q_sldstn = q_sldstn;
    }

    /**
     * Returns the number of pcs available in the room
     * @return number of pcs available
     */
    public int getQ_pc() {
        return q_pc;
    }

    /**
     * this method sets the number of pcs, which should be placed in a room
     * @param q_pc
     * stands for the number of pcs
     */
    public void setQ_pc(int q_pc) {
        this.q_pc = q_pc;
    }

    /**
     * Returns the number of soldering stations available in the room
     * @return number of soldering stations
     */
    public int getQ_sldstn() {
        return q_sldstn;
    }

    /**
     * this method sets the number of soldering stations
     * which should be placed in a room
     * @param q_sldstn
     * stands for the number of soldering stations
     */
    public void setQ_sldstn(int q_sldstn) {
        this.q_sldstn = q_sldstn;
    }

    /**
     * Returns the number of beamers available in the room
     * @return number of beamers
     */
    public int getQ_beamer() {
        return q_beamer;
    }

    /**
     * this method sets the number of beamers 
     * which should be placed in a room
     * @param q_beamer
     * stands for the number of beamers
     */
    public void setQ_beamer(int q_beamer) {
        this.q_beamer = q_beamer;
    }

    /**
     * Returns the number of boards available in the room
     * @return number of boards
     */
    public int getQ_board() {
        return q_board;
    }

    /**
     * this method sets the number of boards 
     * which should be placed in a room
     * @param q_board
     * stands for the number of boards
     */
    public void setQ_board(int q_board) {
        this.q_board = q_board;
    }

    /**
     * Returns the id of the roomobject
     * @return id of the roomobject
     */
    public Integer getId() {
        return id;
    }

    /**
     * this method sets the id for a room object
     * @param id
     * stands for the id, a room objekt should have
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
