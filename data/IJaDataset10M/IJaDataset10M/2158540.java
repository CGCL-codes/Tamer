package rtm4java;

import java.util.Collection;

/**
 * Describes the operations of a note (as in annotation)
 * 
 * @author <a href="mailto:nerab@gmx.at">Andreas E. Rabenau</a>
 */
public interface Annotated {

    public void addNotes(Collection<Note> notes);

    public void setNotes(Collection<Note> notes);

    public void removeNotes(Collection<Note> notes);
}
