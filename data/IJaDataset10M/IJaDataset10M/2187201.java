/*
 * $Id:HtmlParserSyosai.java Rev 2008/08/14 taeda $
 */

package matuya.analyze.syosai;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import matuya.MUtil;


/**
 * <p>TODO �N���X��</p>
 * �����E�ӔC�E�g�p�@�̐��������邱�ƁB<br/>
 *
 * @auther solxyz co.
 * @version 1.0
 */
public class HtmlParserSyosai {
	String content_=null;
	
	/** �\�� */
	String kouzou_=null;
	
	/** ����  */
	String genjyo_=null;
	
	/** �z�N��  */
	String chikuNengetu_=null;
	
	/** �ݔ� */
	String setubi_=null;
	
	/** ��� */
	String koutu_=null;
	
	/** �ڍ׃y�[�W�̓��e���Z�b�g���� */
	public void setContent(String _content){
		content_=_content;
	}
	
	public boolean analyze(){
		boolean ret = false;
		Reader reader = null;
		try{
			reader = new StringReader(content_);
			SyosaiCallback scb = new SyosaiCallback();
			ParserDelegator pd = new ParserDelegator();
			pd.parse(reader, scb, true);
			
			kouzou_      =scb.getKouzou      ();//�\��
			genjyo_      =scb.getGenjyo      ();//����
			chikuNengetu_=scb.getChikuNengetu();//�z�N��
			setubi_      =scb.getSetubi      ();//�ݔ�
			koutu_       =scb.getKoutu       ();//���
			
			ret = true;
		}catch(Exception e){
			e.printStackTrace();
			ret = false;
		}finally{
			if(reader!=null){
				try{
					reader.close();
				}catch(IOException e){}
			}
		}
		
		return ret;
	}
	
	/** �u�\���v��Ԃ� */
	public String getKouzou(){
		return kouzou_;
	}
	
	/** �u����v��Ԃ� */
	public String getGenjyo(){
		return genjyo_;
	}
	
	/** �u�z�N���v��Ԃ� */
	public String getChikuNengetu(){
		return chikuNengetu_;
	}
	
	/** �u�ݔ��v��Ԃ� */
	public String getSetubi(){
		return setubi_;
	}
	/** �u��ʁv��Ԃ�  */
	public String getKoutu(){
		return koutu_;
	}
	/**
	 * <p>TODO ��������B</p>
	 * <pre>
	 * TODO �������e���L�q�B
	 * </pre>
	 * @param args
	 */
	public static void main(String[] args)
	
	{
		final String CRLF = MUtil.CRLF;
		
		String filename = "doc/P1_1/1_2_59.html";
		
		StringBuffer sb = new StringBuffer();
		InputStreamReader reader = null;
		BufferedReader    br     = null;
		try {
			//InputStream in = new InputStream(new File(filename).toURI().toURL().openStream());
			reader = new InputStreamReader(
					new File(filename).toURI().toURL().openStream(), 
					"Windows-31J");
			br = new BufferedReader(reader);
			String tmp=null;
			while((tmp=br.readLine())!=null){
				sb.append(tmp).append(CRLF);
			}
		} catch (MalformedURLException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
			return;
		}finally{
			if(br!=null){
				try{
					br.close();
				}catch(Exception e){}
			}
			if(reader!=null){
				try{
					reader.close();
				}catch(Exception e){}
			}
		}
		
		
		String content = sb.toString();
		
		HtmlParserSyosai parser = new HtmlParserSyosai();
		parser.setContent(content);
		
		if(parser.analyze()){
			System.out.println("�\���@��"+parser.getKouzou      ());
			System.out.println("����@��"+parser.getGenjyo      ());
			System.out.println("�z�N����"+parser.getChikuNengetu());
			System.out.println("�ݔ��@��"+parser.getSetubi      ());
			System.out.println("��ʁ@��"+parser.getKoutu       ());
		}

	}
	

}

class SyosaiCallback extends HTMLEditorKit.ParserCallback{
		/** td�^�O�ɓ����� */
		boolean inTd_ = false;
		
		//MyDataSet osusumeDs_=new MyDataSet("�\��");
		MyDataSet kouzouDs_      =new MyDataSet("�\��");
		MyDataSet genjyoDs_      =new MyDataSet("����");
		MyDataSet chikuNengetuDs_=new MyDataSet("�z�N��");
		MyDataSet setubiDs_      =new MyDataSet("�ݔ�");
		MyDataSet koutuDs_       =new MyDataSet("���");
		
		
		public String getKouzou(){
			return kouzouDs_.getTargetVariable();
		}
		public String getGenjyo(){
			return genjyoDs_.getTargetVariable();
		}
		public String getChikuNengetu(){
			return chikuNengetuDs_.getTargetVariable();
		}
		public String getSetubi(){
			return setubiDs_.getTargetVariable();
		}
		public String getKoutu(){
			return koutuDs_.getTargetVariable();
		}
		
		public void handleStartTag(HTML.Tag _tag, MutableAttributeSet _attr, int _pos){
			if(HTML.Tag.TR.equals(_tag)){
				inTd_=true;
			}
		}
		
		public void handleEndTag(HTML.Tag _tag, int _pos){
			if(HTML.Tag.TR.equals(_tag)){
				inTd_=false;
			}
		}
		
		public void handleText(char[] _c, int _pos){
			//TODO taeda handleText
			if(inTd_){
				kouzouDs_      .examin(new String(_c));
				genjyoDs_      .examin(new String(_c));
				chikuNengetuDs_.examin(new String(_c));
				setubiDs_      .examin(new String(_c));
				koutuDs_       .examin(new String(_c));
//				String tmp = new String(_c);
//				if(flgKouzou_){
//					kouzou_=tmp;
//					flgKouzou_=false;
//				}else{
//					if("�\��".equals(tmp)){
//						flgKouzou_=true;
//					}
//				}
			}
			
		}
	}

class MyDataSet{
	String targetWord_=null;
	boolean flg_=false;
	String targetVariable_=null;
	
	MyDataSet(String _targetWord){
		targetWord_=_targetWord;
	}
	
	void examin(String _val){
		if(getFlg()){
			setTargetVariable(_val);
			setFlg(false);
		}else{
			if(targetWord_.equals(_val)){
				setFlg(true);
			}
		}
	}
	
	void setFlg(boolean _flg){
		flg_=_flg;
	}
	boolean getFlg(){
		return flg_;
	}
	void setTargetVariable(String _val){
		targetVariable_=_val;
	}
	String getTargetVariable(){
		return targetVariable_;
	}
}
