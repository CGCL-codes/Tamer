/*
 * Copyright 2005 Olivier HOAREAU All rights reserved.
 * Use is subject to General Public License terms.
 */

package org.warko.dapp;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;

import org.warko.app.Application;
import org.warko.app.ApplicationException;
import org.warko.app.ApplicationResourceImporter;
import org.warko.app.ApplicationResourceManager;
import org.warko.app.TypedApplicationParameterList;

/**
 * The <code>GenericResourceManager</code> interface ...
 * 
 * TODO better description.
 * 
 * @author  Olivier Hoareau
 * @version 0.1, 02/14/05
 * @since   JDK1.4
 */

public class GenericResourceManager implements ApplicationResourceManager {

	/**
	 * TODO
	 */
	private Application app;

	/**
	 * TODO
	 * 
	 * @param app
	 */
	public GenericResourceManager(Application app) {
		
		this.app=app;
		
	}

	/**
	 * TODO
	 * 
	 * @param file
	 * @param pl
	 * @return
	 */
	public ApplicationResourceImporter createXmlImporter(String file){
		
		return this.createXmlImporter(file,this.getApplication());
		
	}

	/**
	 * TODO
	 * 
	 * @param file
	 * @return
	 */
	public ApplicationResourceImporter createXmlImporter(String file,TypedApplicationParameterList pl){
		
		return GenericXmlImporter.createImporter(this.getApplication(),this.getResourceAsString(file),pl);
		
	}
	
	public String getResourceAsString(String fileName) {
	
		BufferedReader br = null;
		String res = "";
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
			String line = null;
			while((line=br.readLine())!=null){
				res+=(res.length()>0?"\n":"")+line;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * TODO
     * 
	 * @return application.
	 */
	public Application getApplication() {
		
		return this.app;
		
	}

	/**
	 * TODO
	 * 
	 * @return bytes.
	 */
	public String dump() {
	
		String s = "";
		
		s += "{";
		s += "app="+this.app;
		s += "}";
		
		return s;
		
	}

	public void loadFile(URL file, ApplicationResourceImporter importer) throws ApplicationException {
		importer.importResource(file);
	}
	
	/**
	 * Loads all the classes contained in the specified jar file.
	 */
	public ClassLoader getJarClassLoader(String jarFile) {
		
		JarFile jf= null;
		try{
			jf = new JarFile(jarFile);
		}catch(Throwable e){
			e.printStackTrace();
		}
		if(jf!=null){
			GenericURLClassLoader cl = new GenericURLClassLoader(this.getApplication(),new URL[]{});
			try{
				cl.addNewURL(new File(jarFile).toURL());
			}catch(Throwable e){
				e.printStackTrace();
			}
			for(Enumeration enum=jf.entries();enum.hasMoreElements();){
				ZipEntry ze = (ZipEntry) enum.nextElement();
				if(ze.getName().endsWith(".class")){
					try{
						cl.loadClass(ze.getName().replaceAll("/", ".").substring(0,
								ze.getName().length() - 6));
					}catch(Throwable e){
						e.printStackTrace();
					}
				}
			}
			return cl;
		}
		return null;
	}

	public URL getResourceAsURL(String act) {
		
		URL url = null;
		act = act.replace("\\".charAt(0),"/".charAt(0));
		if(act.startsWith("jar:")){
			
		}else if(act.startsWith("file:")){
			
		}else{
			act = "file:"+act;
		}

		try{
			url = new URL(act);
			// TODO
			// remember to return null if the current url is not
			// an existing url.
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		return url;
		
	}

	public Image getResourceAsImage(String name) {
		
		Image i = null;
		try{
			i = ImageIO.read(this.getResourceAsURL(name));
		}catch(Exception e){
			e.printStackTrace();
		}
		return i;
		
	}

}
