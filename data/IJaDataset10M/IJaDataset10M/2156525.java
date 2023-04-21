package server;

import objects.Race;

public class SetDiplomacyStateCommand extends AbstractCommand {

    final int state;

    public SetDiplomacyStateCommand(int state) {
        this.state = state;
    }

    @Override
    public final boolean doIt(String[] cmd, Race r) {
        boolean result = false;
        switch(cmd.length) {
            case 2:
                String raceName = r.galaxy.validIdentifier(cmd[1]);
                cmd[1] = raceName;
                Race race = r.galaxy.getRaceByName(raceName);
                if (race == null) {
                    setErrorMessage("Race not found - " + raceName);
                    break;
                }
                if (r.name.equals(race.name)) r.setAllDiplomacyState(state); else r.setDiplomacyState(race, state);
                result = true;
                break;
        }
        return result;
    }
}
