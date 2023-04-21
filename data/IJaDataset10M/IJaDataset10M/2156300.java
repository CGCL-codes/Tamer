package javacommon.util.xml;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** 
 * <title>使用XML文件存取可序列化的对象的类</title> 
 * <description>提供保存和读取的方法</description> 
 * @author 殷晋 
 * <copyright>清华大学汽车工程开发研究院@2005</copyright> 
 * @version 1.0 
 * 2005-8-5 16:44:49 
 */
public class ObjectToXMLUtil {

    /** 
	 * 把java的可序列化的对象(实现Serializable接口)序列化保存到XML文件里面,如果想一次保存多个可序列化对象请用集合进行封装 
	 * 保存时将会用现在的对象原来的XML文件内容 
	 * @param obj 要序列化的可序列化的对象 
	 * @param fileName 带完全的保存路径的文件名  
	 * @throws FileNotFoundException 指定位置的文件不存在 
	 * @throws IOException 输出时发生异常 
	 * @throws Exception 其他运行时异常 
	 */
    public static void objectXmlEncoder(Object obj, String fileName) throws FileNotFoundException, IOException, Exception {
        File fo = new File(fileName);
        if (!fo.exists()) {
            String path = fileName.substring(0, fileName.lastIndexOf('.'));
            File pFile = new File(path);
            pFile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(fo);
        XMLEncoder encoder = new XMLEncoder(fos);
        encoder.writeObject(obj);
        encoder.flush();
        encoder.close();
        fos.close();
    }

    /** 
	 * 读取由objSource指定的XML文件中的序列化保存的对象,返回的结果经过了List封装 
	 * @param objSource 带全部文件路径的文件全名 
	 * @return 由XML文件里面保存的对象构成的List列表(可能是一个或者多个的序列化保存的对象)   
	 * @throws FileNotFoundException 指定的对象读取资源不存在 
	 * @throws IOException 读取发生错误 
	 * @throws Exception 其他运行时异常发生 
	 */
    public static List objectXmlDecoder(String objSource) throws FileNotFoundException, IOException, Exception {
        List objList = new ArrayList();
        File fin = new File(objSource);
        FileInputStream fis = new FileInputStream(fin);
        XMLDecoder decoder = new XMLDecoder(fis);
        Object obj = null;
        try {
            while ((obj = decoder.readObject()) != null) {
                objList.add(obj);
            }
        } catch (Exception e) {
        }
        fis.close();
        decoder.close();
        return objList;
    }
}
