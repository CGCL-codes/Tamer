package es.phoneixs.reversewords.modelo.transformadores;

import es.phoneixs.reversewords.modelo.Transformador;

public class Invertir implements Transformador {

	@Override
	public String transforma(String textoOriginal) {
		StringBuffer norm = new StringBuffer();
		
		for (int i = textoOriginal.length(); i > 0 ; i--) {
			norm.append(textoOriginal.substring(i-1, i));
		}
		
		return norm.toString();
	}

}
