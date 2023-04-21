//    pvoim - program for text, voice and probably other type of media messaging
//    Copyright (C) 2009  Pechenko Anton Vladimirovich aka Parilo
//    mailto: forpost78 at mail dot ru
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>
//
//    Sponsored by SibEK
//    Official site: www.sibek.ru
//

package pvoim.sound_coder;

import org.xiph.speex.Decoder;
import org.xiph.speex.NbDecoder;
import org.xiph.speex.SpeexDecoder;
import org.xiph.speex.spi.*;

import java.io.*;

import javax.sound.sampled.*;

import pvoim.main.pvoim_main;

public class Snd_Coder {
	
	private byte[] buffer;
	private AudioFormat af;
        
        private pvoim_main pm;
	
	public Snd_Coder(pvoim_main pm_){
                
                this.pm = pm_;
            
		this.buffer = new byte[512];
		this.af = new AudioFormat(8000, 16, 1, true, false);

	}
	
	public byte[] Encode(byte[] pcm){
		
		ByteArrayOutputStream ogg_speex_buf = new ByteArrayOutputStream();
		ByteArrayInputStream pcm_stream = new ByteArrayInputStream(pcm);
		
		Pcm2SpeexAudioInputStream SpeexEnc = new Pcm2SpeexAudioInputStream(0,8,pcm_stream,this.af,pcm.length/2);
		
		try{
			int ret=0;
			do{
				ogg_speex_buf.write(this.buffer, 0, ret);
				ret = SpeexEnc.read(this.buffer, 0, 512);
			} while(ret!=-1);
		} catch(IOException e){
			System.out.println("Snd_Coder: Encode: IOException: "+e.getMessage());
		}
		
		byte[] ogg_speex = ogg_speex_buf.toByteArray();
		return ogg_speex;
	}
	
	public byte[] Decode(byte[] encoded){
		
		
		System.out.println("Snd_Coder: got: "+encoded.length+" bytes");
		
		see JSpeexDec.java and include
		
//		this.switchEndianness(encoded);
		
/*		ByteArrayOutputStream pcm_buf = new ByteArrayOutputStream();
		ByteArrayInputStream encoded_stream = new ByteArrayInputStream(encoded);
		
		System.out.println("Snd_Coder: before");
//		Speex2PcmAudioInputStream SpeexDec = new Speex2PcmAudioInputStream(encoded_stream,this.af,1);
		Speex2PcmAudioInputStream SpeexDec = new Speex2PcmAudioInputStream(encoded_stream,this.af,AudioSystem.NOT_SPECIFIED);
		System.out.println("Snd_Coder: after");
		
		try{
			int ret=0;
			do{
				System.out.println("Snd_Coder: loop: 1");
				pcm_buf.write(this.buffer, 0, ret);
				System.out.println("Snd_Coder: loop: 2");
//				ret = SpeexDec.read(this.buffer, 0, 512);
				System.out.println("Snd_Coder: avail: "+SpeexDec.available());
				ret = SpeexDec.read(this.buffer, 0, SpeexDec.available());
				System.out.println("Snd_Coder: ret: "+ret);
			} while(ret!=-1);
		} catch(IOException e){
			System.out.println("Snd_Coder: Decode: IOException: "+e.getMessage());
		}*/
		
		
		
		/*System.out.println("Snd_Coder: 1");
		SpeexDecoder sd = new SpeexDecoder();
		System.out.println("Snd_Coder: 2");
		if( !sd.init(0, 8000, 1, false) ){
			System.out.println("Snd_Coder: jspeex init failed");
		}
		System.out.println("Snd_Coder: 3");
		
		try {
			sd.processData(encoded, 0, 160);
		} catch (StreamCorruptedException e) {
			System.out.println("Snd_Coder: speex corrupted: "+e.getMessage());
		}
		System.out.println("Snd_Coder: 4");
		
		System.out.println("Snd_Coder: 5");
		byte[] out = new byte[sd.getProcessedDataByteSize()];
		System.out.println("Snd_Coder: 6");
		sd.getProcessedData(out, 0);
		System.out.println("Snd_Coder: 7");*/
		
		
		/*System.out.println("Snd_Coder: decoded: "+pcm_buf.toByteArray().length+" bytes");
		return pcm_buf.toByteArray();*/
		
		/*System.out.println("Snd_Coder: decoded: "+out.length+" bytes");
		return out;*/
		
		
		
	}
	
/*    private void switchEndianness(byte[] samples) {
        for (int i = 0; i < samples.length; i += 2) {
        	System.out.println(i+" ");
            byte tmp = samples[i];
            samples[i] = samples[i + 1];
            samples[i + 1] = tmp;
        }
    }*/	
}
