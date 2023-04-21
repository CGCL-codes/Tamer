package wcp.model.events;

import wcp.model.events.Participant;

/**
 * @hibernate.class table = "PARTICIPANT"
 * 
 * @author timconinx
 *
 */
public class ParticipantImpl implements Participant {

    private Long id;

    private Character character;

    /**
	 * @hibernate.id column = "ID"
	 * 				generator-class = "increment"
	 * @return
	 */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @hibernate.one-to-one 
	 * 						class = "wcp.model.playersguilds.CharacterImpl"
	 * @return
	 */
    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}
