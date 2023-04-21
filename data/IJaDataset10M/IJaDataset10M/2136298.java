package org.xmlsh.aws;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import net.sf.saxon.s9api.SaxonApiException;
import org.xmlsh.aws.util.AWSEC2Command;
import org.xmlsh.core.InvalidArgumentException;
import org.xmlsh.core.Options;
import org.xmlsh.core.UnexpectedException;
import org.xmlsh.core.XValue;
import org.xmlsh.sh.shell.SerializeOpts;
import org.xmlsh.util.Util;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;

public class ec2StopInstances extends AWSEC2Command {

    /**
	 * @param args
	 * @throws IOException 
	 */
    @Override
    public int run(List<XValue> args) throws Exception {
        Options opts = getOptions();
        opts.parse(args);
        args = opts.getRemainingArgs();
        if (args.size() < 1) {
            usage(null);
            return 1;
        }
        mSerializeOpts = this.getSerializeOpts(opts);
        try {
            mAmazon = getEC2Client(opts);
        } catch (UnexpectedException e) {
            usage(e.getLocalizedMessage());
            return 1;
        }
        int ret = stop(Util.toStringArray(args));
        return ret;
    }

    private int stop(String[] instances) throws InvalidArgumentException, IOException, XMLStreamException, SaxonApiException {
        StopInstancesRequest request = new StopInstancesRequest(Arrays.asList(instances));
        StopInstancesResult result = mAmazon.stopInstances(request);
        List<InstanceStateChange> changes = result.getStoppingInstances();
        writeStateChages(changes);
        return 0;
    }
}
