package ru.syktsu.projects.oko2.server.interfaces;

import java.util.List;
import ru.syktsu.projects.oko2.server.objects.Participant;
import ru.syktsu.projects.oko2.server.objects.Solution;

/**
 * Интерфейс взаимодействия с участниками (interface to operate with participants)
 * 
 * @author Konst
 */
public interface IParticipant {

    public List<Solution> getSolutions(String participantId, Integer fromPosition, Integer quantity);

    public void update(Participant participant);
}
