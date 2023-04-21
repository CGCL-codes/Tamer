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
 * <p>TODO クラス名</p>
 * 役割・責任・使用法の説明をすること。<br/>
 *
 * @auther solxyz co.
 * @version 1.0
 */
public class HtmlParserSyosai {
	String content_=null;
	
	/** 構造 */
	String kouzou_=null;
	
	/** 現状  */
	String genjyo_=null;
	
	/** 築年月  */
	String chikuNengetu_=null;
	
	/** 設備 */
	String setubi_=null;
	
	/** 交通 */
	String koutu_=null;
	
	/** 詳細ページの内容をセットする */
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
			
			kouzou_      =scb.getKouzou      ();//構造
			genjyo_      =scb.getGenjyo      ();//現状
			chikuNengetu_=scb.getChikuNengetu();//築年月
			setubi_      =scb.getSetubi      ();//設備
			koutu_       =scb.getKoutu       ();//交通
			
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
	
	/** 「構造」を返す */
	public String getKouzou(){
		return kouzou_;
	}
	
	/** 「現状」を返す */
	public String getGenjyo(){
		return genjyo_;
	}
	
	/** 「築年月」を返す */
	public String getChikuNengetu(){
		return chikuNengetu_;
	}
	
	/** 「設備」を返す */
	public String getSetubi(){
		return setubi_;
	}
	/** 「交通」を返す  */
	public String getKoutu(){
		return koutu_;
	}
	/**
	 * <p>TODO 処理する。</p>
	 * <pre>
	 * TODO 処理内容を記述。
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
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
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
			System.out.println("構造　＝"+parser.getKouzou      ());
			System.out.println("現状　＝"+parser.getGenjyo      ());
			System.out.println("築年月＝"+parser.getChikuNengetu());
			System.out.println("設備　＝"+parser.getSetubi      ());
			System.out.println("交通　＝"+parser.getKoutu       ());
		}

	}
	

}

class SyosaiCallback extends HTMLEditorKit.ParserCallback{
		/** tdタグに入った */
		boolean inTd_ = false;
		
		//MyDataSet osusumeDs_=new MyDataSet("構造");
		MyDataSet kouzouDs_      =new MyDataSet("構造");
		MyDataSet genjyoDs_      =new MyDataSet("現状");
		MyDataSet chikuNengetuDs_=new MyDataSet("築年月");
		MyDataSet setubiDs_      =new MyDataSet("設備");
		MyDataSet koutuDs_       =new MyDataSet("交通");
		
		
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
//					if("構造".equals(tmp)){
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
