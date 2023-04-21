package mypack;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLTTransformer {

	public void transform() {
		try {
			TransformerFactory  tFactory =  TransformerFactory.newInstance();
			Source xslSource = new StreamSource( "src/xml/applicantprofileTransform.xsl" );
                        //Source xslSource = new StreamSource( "src/xml/applicantprofileWithGPA.xsl" );
			Transformer transformer = tFactory.newTransformer( xslSource );
			transformer.transform( new StreamSource( "src/xml/resume.xml" ),new StreamResult( new FileOutputStream( "src/xml/userProfile.xml" )));
                        //transformer.transform( new StreamSource( "src/xml/transcript.xml" ),new StreamResult( new FileOutputStream( "src/xml/userProfile.xml" )));
		}catch(TransformerFactoryConfigurationError | FileNotFoundException | TransformerException ex) {}
	}
}
