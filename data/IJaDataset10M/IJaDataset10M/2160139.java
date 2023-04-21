package org.esprit.ocm.server.metier.ec2;

import org.esprit.ocm.dto.impl.AwsCredentialsDto;
import org.esprit.ocm.dto.impl.ServerDto;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.xerox.amazonws.ec2.ConsoleOutput;

@RemoteServiceRelativePath("ConsoleService.rpc")
public interface ConsoleService extends RemoteService {

    /**
	 * Get console ouput for a particular instance.
	 * 
	 * @param instanceId
	 *            The instanceId of the instance to get the console output from.
	 * @return ConsoleOutput object containing output information or null in
	 *         case of error.
	 */
    public ConsoleOutput getConsoleOutput(String instanceId, ServerDto server, AwsCredentialsDto credentials);
}
