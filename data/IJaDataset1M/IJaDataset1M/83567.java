package dk.knord.s3f09.servlets.commands;

import dk.knord.s3f09.backend.MapRegistrationRepository;
import dk.knord.s3f09.backend.MemberImplementation;
import dk.knord.s3f09.backend.TeamImplementation;
import dk.knord.s3f09.servlets.CommandException;
import dk.knord.s3f09.servlets.Factory;
import dk.knord.s3f09.servlets.RegistrationRepository;
import dk.knord.s3f09.sportsclub.contract.Member;
import dk.knord.s3f09.sportsclub.contract.Team;
import javax.servlet.http.HttpServletRequest;

public class addCommand extends TargetCommand {

    public addCommand(String target) {
        super(target);
    }

    public String execute(HttpServletRequest request) throws CommandException {
        RegistrationRepository repository = Factory.getInstance().getRepository();
        Team team = repository.loadTeam(request.getParameter("teamCombo"));
        Member member = repository.loadMember(request.getParameter("memberCombo"));
        ((MapRegistrationRepository) repository).addConnection(member, team);
        repository.save(team);
        return super.execute(request);
    }
}
