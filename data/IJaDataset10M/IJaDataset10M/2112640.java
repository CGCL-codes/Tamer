/*
 * Created on 01/04/2004 19:27:49
 * Motorola Brazil Design Center
 * Software Test Program
 * CIn / UFPE / Brazil
 * */
 
package br.ufpe.cin.imersion.pattern.behavioral;

import java.io.FileNotFoundException;

/**
 * @author Marcello Alves de Sales Junior <BR>
 * email: masj2@cin.ufpe.br <BR>
 * 01/04/2004 
 */
public class Conta {

	String nome;

	public void cred(){
		System.out.println("em conta");
	}
	
	//private abstract void uii();
	
	public static void deb(){
		System.out.println("deb conta");
	}

	public static void main(String[] args) {
		
		Conta a, b;
		a = new Poupança();
		b = new Conta();
		//a.cred();
		//b.cred();
		//a.deb();
		//b.deb();				
	}
}

class Poupança extends Conta{
	
	public void cred(){
		System.out.println("em poup");
		
	}

	public static void deb(){
		System.out.println("deb poup");
	}	
	
}
