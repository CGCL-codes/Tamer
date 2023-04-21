package jawara;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import java.util.Enumeration;

import net.jxta.platform.Application;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.Message;
import net.jxta.id.IDFactory;
import net.jxta.id.ID;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
//import net.jxta.pipe.Pipe;
import net.jxta.pipe.PipeID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.protocol.PeerGroupAdvertisement;
//import net.jxta.protocol.ServiceAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.discovery.DiscoveryService;
import net.jxta.exception.PeerGroupException;
import net.jxta.impl.peergroup.Platform;
import net.jxta.impl.peergroup.GenericPeerGroup;

import gnu.mapping.*;
import gnu.lists.*;
import gnu.expr.*;
import gnu.text.*;
import kawa.standard.*;
import kawa.lang.*;

public class JawaraFindPipeAdv extends Procedure1
{
    public Object apply1 (Object arg)
    {
	String name = ((FString)arg).toString ();
	DiscoveryService discovery = Jawara.group.getDiscoveryService ();
	Enumeration enum;

	System.out.print("Searching for "+name+"...");
	System.out.flush();
	while (true)
	    {
		try
		    {
			enum = discovery.getLocalAdvertisements
			    (DiscoveryService.ADV, "Name", name);
			if ((enum != null) && enum.hasMoreElements()) break;
			discovery.getRemoteAdvertisements
			    (null, DiscoveryService.ADV, "Name", name, 1,
			     null);
			try
			    {
				Thread.sleep(2000);
			    }
			catch (Exception e)
			    {}
		    }
		catch (Exception e)
		    {}
		System.out.print(".");
		System.out.flush();
	    }
	System.out.println("done.");
	
	PipeAdvertisement pipeadv = (PipeAdvertisement)
	    enum.nextElement();	
	System.out.println("Found PipeID="+pipeadv.getPipeID());
	return pipeadv;
    }
}
