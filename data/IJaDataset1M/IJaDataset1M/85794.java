package colonsdelutbm;

import javax.swing.JOptionPane;

import colonsdelutbm.ControleContinu.TypeCC;
import colonsdelutbm.UV.TypeUV;

/** Class qui tout les informations du plateaux, hexagones, UV, CC, joueurs*/
public class Plateau {
	
	private double Taille;
	protected int nbJoueur;
	protected Hexagone[] tabHexagone;
	protected HexagoneExt[] tabHexagoneExt;
	protected UV[] tabUV;
	protected int UVstatus; /**Pour savoir si l'on doit dessiner les UVS non posés ou pas */
	protected ControleContinu[] tabControle;
	
	 /**Coordonnée principales*/
    public static int coordx = 330; /**coordonnée du premier hexagone*/
    public static int coordy = 150; /**coordonnée du premier hexagone*/
    public static int decalX = 105; /**espacement en X*/
    public static int decalY = 45; /**espacement en Y*/
    public static int centreY = 4*decalY + coordy;
    public static int centreX = decalX/2 + coordx;
    public static int rayonG = (int)Math.rint(Math.sqrt( (6 * decalY)*(6 * decalY) + (decalX/2)*(decalX/2)));//rayon pour les hexagones extérieurs
    
	/**
	 * 
	 * @param plateauImp 
	 * @return 
	 */
	public Plateau(int nbJoueur) {
		
		//Création des joueur
		this.nbJoueur=nbJoueur;
		
		/**Appel de la fonction qui créé le tableau des hexagones intérieurs*/
		ConstructHexagone();
		
		/**Appel de la fonction qui créée le tableau des hexagones extérieurs*/
		ConstructHexagoneExt();
		
		/**Appel de la fonction qui créé le tableau des UV possibles*/
		ConstructUV();
		
		/**FOnction qui créé le tableau des controles continus possibles*/
		ConstructControle();	
		
			
	}
	

	/** Constructeur par recopie*/
public Plateau(Plateau plateauImp) {
	
		tabHexagone = new Hexagone[20];
		tabHexagoneExt = new HexagoneExt[18];
		tabUV =  new UV[54];
		tabControle = new ControleContinu[94];
		
		for(int i = 0; i < 19; i++){
			tabHexagone[i] = new Hexagone(plateauImp.tabHexagone[i]);
		}
		
		for(int i = 0; i < 18; i++){
			tabHexagoneExt[i] = new HexagoneExt(plateauImp.tabHexagoneExt[i]);
		}
	
		for(int i = 0; i < 54; i++){
			
				if( plateauImp.tabUV[i] != null){
					tabUV[i] = new UV(plateauImp.tabUV[i]);
				}
		}
		
		for(int i = 0; i < 94; i++){
			
			if( plateauImp.tabControle[i] != null){
				tabControle[i] = new ControleContinu(plateauImp.tabControle[i]);
			}
	}
		
		UVstatus = plateauImp.UVstatus;
		
		
		
	}

/** fonction qui créé le tableau des hexagones intérieurs */
protected void ConstructHexagone(){
	
	//On definit le nombres de types a attribuer
	int[] type = new int[5];
	type[0] = 4;
	type[1] = 4;
	type[2] = 4;
	type[3] = 3;
	type[4] = 3;
	
	//On definit le nombres de jeton à attribuer
	int[] jeton = new int[13];
	jeton[0] = 0;
	jeton[1] = 0;
	jeton[2] = 1;
	jeton[3] = 2;
	jeton[4] = 2;
	jeton[5] = 2;
	jeton[6] = 2;
	jeton[7] = 0;
	jeton[8] = 2;
	jeton[9] = 2;
	jeton[10] = 2;
	jeton[11] = 2;
	jeton[12] = 1;
	
	int rand;
	
	tabHexagone = new Hexagone[19]; //tableau des hexagones


                    
  //On contruit la premiÃ¨re rangÃ©e d'hexagone Ã  partir de celui de tout en haut
	for(int j = 0; j < 3; j++){
		tabHexagone[j] = new Hexagone( coordx + j*decalX, coordy + j*decalY );
		
	do{
		rand = (int) (Math.random()*5);
	}
	while(type[rand] == 0);
	
	type[rand] -= 1;
	tabHexagone[j].setProduction(rand);
	
	do{
		rand = (int) (Math.random()*13);
	}
	while(jeton[rand] == 0);
	
	jeton[rand] -= 1;
	tabHexagone[j].jeton = rand;
	
	}
                
                //On construit la deuxiÃ¨me rangÃ©e en dÃ©calant les x Ã  gauche et en bas
                for(int j = 3; j <7 ; j++){
		tabHexagone[j] = new Hexagone(coordx - 105 + (j-3)*decalX,coordy + 45 + (j-3)*decalY  );
		
			do{
				rand = (int) (Math.random()*5);
			}
			while(type[rand] == 0);
			
			type[rand] -= 1;
			tabHexagone[j].setProduction(rand);
			
			do{
				rand = (int) (Math.random()*13);
			}
			while(jeton[rand] == 0);
			
			jeton[rand] -= 1;
			tabHexagone[j].jeton = rand;
		
	}

                //On construit la troisiÃ¨me rangÃ©e en dÃ©calant les x Ã  gauche et en bas
                for(int j = 7; j <12 ; j++){
		tabHexagone[j] = new Hexagone(coordx - 210 + (j-7)*decalX,coordy + 90 + (j-7)*decalY );
		if(j == 9){
			tabHexagone[j].setProduction(5);
			tabHexagone[j].jeton = 7;
		}else
		{
			do{
				rand = (int) (Math.random()*5);
			}
			while(type[rand] == 0);
			
			type[rand] -= 1;
			tabHexagone[j].setProduction(rand);
			
			do{
				rand = (int) (Math.random()*13);
			}
			while(jeton[rand] == 0);
			
			jeton[rand] -= 1;
			tabHexagone[j].jeton = rand;
		}
	}

                //On construit la 4Ã¨me rangÃ©e en dÃ©calant les x Ã  gauche et en bas
                for(int j = 12; j <16 ; j++){
		tabHexagone[j] = new Hexagone(coordx - 210 + (j-12)*decalX,coordy + 180 + (j-12)*decalY );
		do{
			rand = (int) (Math.random()*5);
		}
		while(type[rand] == 0);
		
		type[rand] -= 1;
		tabHexagone[j].setProduction(rand);
		
		do{
			rand = (int) (Math.random()*13);
		}
		while(jeton[rand] == 0);
		
		jeton[rand] -= 1;
		tabHexagone[j].jeton = rand;
	}

                //On construit la 5Ã¨me rangÃ©e en dÃ©calant les x Ã  gauche et en bas
                for(int j = 16; j <19 ; j++){
		tabHexagone[j] = new Hexagone(coordx - 210 + (j-16)*decalX,coordy + 270 + (j-16)*decalY );
		do{
			rand = (int) (Math.random()*5);
		}
		while(type[rand] == 0);
		
		type[rand] -= 1;
		tabHexagone[j].setProduction(rand);
		
		do{
			rand = (int) (Math.random()*13);
		}
		while(jeton[rand] == 0);
		
		jeton[rand] -= 1;
		tabHexagone[j].jeton = rand;
	}
	
}

/**fonction qui créée le tableau des hexagones extérieurs*/
protected void ConstructHexagoneExt(){
	tabHexagoneExt = new HexagoneExt[18]; //tableau des hexagones extérieurs 
	
    //On construit les hexagones extérieur avec leurs zones de trocs
   int tab = 0;
   
   //On décal le nouveau point de départ de dessin des hexagones
   int coordxN = coordx;
   int coordyN = coordy - 2*decalY ;
   
   for(int j = 0; j < 6; j++){
	   
	   for(int i = 0; i < 3; i++){
		   
		   
		   switch(j){
		   case 0 :tabHexagoneExt[tab] = new HexagoneExt( coordxN - i*decalX, coordyN  + i*decalY );
		   break;
		   case 1 :tabHexagoneExt[tab] = new HexagoneExt( coordxN - 3*decalX, coordyN + 3*decalY + 2*i*decalY);
		   break;
		   case 2 :tabHexagoneExt[tab] = new HexagoneExt( coordxN - 3*decalX + i*decalX, coordyN + 9*decalY + i*decalY );
		   break;
		   case 3 :tabHexagoneExt[tab] = new HexagoneExt( coordxN + i*decalX, coordyN + 12*decalY - i*decalY );
		   break;
		   case 4 :tabHexagoneExt[tab] = new HexagoneExt( coordxN + 3*decalX, coordyN + 9*decalY - 2*i*decalY);
		   break;
		   case 5 :tabHexagoneExt[tab] = new HexagoneExt( coordxN + 3*decalX - i*decalX, coordyN + 3*decalY - i*decalY );
		   break;
		   default : ;
		   }
		   
		   if( (tab%2) == 1 ){
			   tabHexagoneExt[tab].troc = true;
			   switch(tab){
			   case 1: 
				   tabHexagoneExt[tab].type=0;
				   tabHexagoneExt[tab].taux=2;
				   break;
			   case 3: 
				   tabHexagoneExt[tab].type=2;
				   tabHexagoneExt[tab].taux=3;
				   break;
			   case 5: 
				   tabHexagoneExt[tab].type=3;
				   tabHexagoneExt[tab].taux=2;
				   break;
			   case 7: 
				   tabHexagoneExt[tab].type=1;
				   tabHexagoneExt[tab].taux=3;
				   break;
			   case 9: 
				   tabHexagoneExt[tab].type=3;
				   tabHexagoneExt[tab].taux=3;
				   break;
			   case 11: 
				   tabHexagoneExt[tab].type=2;
				   tabHexagoneExt[tab].taux=2;
				   break;
			   case 13: 
				   tabHexagoneExt[tab].type=0;
				   tabHexagoneExt[tab].taux=3;
				   break;
			   case 15: 
				   tabHexagoneExt[tab].type=1;
				   tabHexagoneExt[tab].taux=2;
				   break;
			   case 17: 
				   tabHexagoneExt[tab].type=4;
				   tabHexagoneExt[tab].taux=2;
				   break;
			   }
		   }
		   tab++;
	   }

   }
	   
  
   
}


/**fonction qui créé le tableau des UV possibles. on crée les coordonnées à partir des hexagones intérieurs, pour cela on peut prend la coordonnée stockée qui est celle de l'extrémité gauche
 * on y soustrait un décalage vers le haut et un autre vers la droite. On ajoute alors les 2 autres coordonnées (des extrémités des deux hexagones adjacents) 
 * pour tracer un triangle. Il faut prendre en compte le fait que pour le point de droite, le triangle sera à droite et pour le point de gauche, le triangle sera sur sa gauche.
 */
protected void ConstructUV(){
	tabUV = new UV[54];
	
	//Coordonnées de décalage pour les trois points du triangle
	int decalYND = - 56;
	int decalYNG = - 45 ;
	int decalYNB = - 34;
	
	int decalXND = 30;
	int decalXNG = 9;
	int decalXNB = 30;
	
	
	
	//Coordonnées de décalage pour le point de droite
	int decalYND1 = decalYNG;
	int decalYNG1 = decalYND;
	int decalYNB1 = decalYNB;
	
	int decalXND1 =  decalXNG + 101;
	int decalXNG1 = decalXND + 60;
	int decalXNB1 = decalXNB + 60;
	
	//Coordonnées de décalage pour le point extrémité droite
	int decalYND2 = decalYND + decalY ;
	int decalYNG2 = decalYNG + decalY ;
	int decalYNB2 = decalYNB + decalY ;
	
	int decalXND2 = decalXND + decalX ;
	int decalXNG2 = decalXNG + decalX ;
	int decalXNB2 =  decalXNB + decalX ;
	
	//Coordonnées de décalage pour le point bas droite
	int decalYND3 = decalYND1 + 90;
	int decalYNG3 = decalYNG1 + 90;
	int decalYNB3 = decalYNB1 + 90;
	
	int decalXND3 = decalXND1;
	int decalXNG3 = decalXNG1;
	int decalXNB3 = decalXNB1;
	
	//Coordonnées de décalage pour le point bas gauche
	int decalYND4 = decalYND + 90;
	int decalYNG4 =  decalYNG + 90;
	int decalYNB4 =  decalYNB + 90;
	
	int decalXND4 = decalXND;
	int decalXNG4 = decalXNG;
	int decalXNB4 = decalXNB;
	
	//Coordonnée de décalage pour le point extrémité gauche
	int decalYND5 = decalYND2;
	int decalYNG5 = decalYNG2;
	int decalYNB5 = decalYNB2;
	
	int decalXND5 =  decalXND2 - 150;
	int decalXNG5 =  decalXNG2 - 107;
	int decalXNB5 =  decalXNB2 - 150;

	
	//Variable qui s'incrément à chaque tour pour construire le tableau des UV
	int nbUV = 0;
	for(int i = 0; i < 19; i++){
		
		/* Cette méthode va consister à créer les UVs pour chaque hexagone et a remplir un tableau de lien vers ces UV à l'intérieur de cette hexagone.
		 *  Pour chaque UVs que l'on créé
		 * on vérifie, en testant les coordonnées des UV, qu'une UV n'a pas déjà été créé. Dans ce cas on
		 * on créé simplement remplit simplement le tableau de l'hexagone par un lien vers UV.
		 */
			
			//Pour l'UV en haut à gauche
			int j = 0;
			//test si l'UV n'existe pas déjà
			if( chercherUV(tabHexagone[i].positionGauche.x + decalXNG, tabHexagone[i].positionGauche.y + decalYNG ) == 100 ){
				
			// on créer l'UV	
			tabUV[nbUV] = new UV(tabHexagone[i].positionGauche.x + decalXNG, tabHexagone[i].positionGauche.y + decalYNG,tabHexagone[i].positionGauche.x + decalXNB, tabHexagone[i].positionGauche.y + decalYNB, tabHexagone[i].positionGauche.x + decalXND, tabHexagone[i].positionGauche.y + decalYND );
			
			// On renseigne tabApartUV de l'hexagone
			tabHexagone[i].tabApartUV[j] = nbUV;
			
			nbUV++;
			}
			
			//donc l'UV existe déjà
			else
			{
				tabHexagone[i].tabApartUV[j] = chercherUV(tabHexagone[i].positionGauche.x + decalXNG, tabHexagone[i].positionGauche.y + decalYNG );
			}
			
			//Pour l'UV en haut à droite
			j = 1;
			//test si l'UV n'existe pas déjà
			if( chercherUV(tabHexagone[i].positionGauche.x + decalXNG1, tabHexagone[i].positionGauche.y + decalYNG1 ) == 100 ){
				
			// on créer l'UV	
			tabUV[nbUV] = new UV(tabHexagone[i].positionGauche.x + decalXNG1, tabHexagone[i].positionGauche.y + decalYNG1, tabHexagone[i].positionGauche.x + decalXNB1, tabHexagone[i].positionGauche.y + decalYNB1, tabHexagone[i].positionGauche.x + decalXND1, tabHexagone[i].positionGauche.y + decalYND1);
			// On renseigne tabApartUV de l'hexagone
			tabHexagone[i].tabApartUV[j] = nbUV;
			
			nbUV++;
			}
			
			//donc l'UV existe déjà
			else
			{
				tabHexagone[i].tabApartUV[j] = chercherUV(tabHexagone[i].positionGauche.x + decalXNG1, tabHexagone[i].positionGauche.y + decalYNG1 );
			}
			
			// pour l'UV à l'extrémité droite
			j = 2;
			//test si l'UV n'existe pas déjà
			if( chercherUV(tabHexagone[i].positionGauche.x + decalXNG2, tabHexagone[i].positionGauche.y + decalYNG2 ) == 100 ){
				
			// on créer l'UV	
			tabUV[nbUV] = new UV(tabHexagone[i].positionGauche.x + decalXNG2, tabHexagone[i].positionGauche.y + decalYNG2, tabHexagone[i].positionGauche.x + decalXNB2, tabHexagone[i].positionGauche.y + decalYNB2, tabHexagone[i].positionGauche.x + decalXND2, tabHexagone[i].positionGauche.y + decalYND2);
			// On renseigne tabApartUV de l'hexagone
			tabHexagone[i].tabApartUV[j] = nbUV;
			
			nbUV++;
			}
			
			//donc l'UV existe déjà
			else
			{
				tabHexagone[i].tabApartUV[j] = chercherUV(tabHexagone[i].positionGauche.x + decalXNG2, tabHexagone[i].positionGauche.y + decalYNG2 );
			}
			
			
			//pour l'UV en bas à droite
			j = 3;
			//test si l'UV n'existe pas déjà
			if( chercherUV(tabHexagone[i].positionGauche.x + decalXNG3, tabHexagone[i].positionGauche.y + decalYNG3 ) == 100 ){
				
			// on créer l'UV	
			tabUV[nbUV] = new UV(tabHexagone[i].positionGauche.x + decalXNG3, tabHexagone[i].positionGauche.y + decalYNG3, tabHexagone[i].positionGauche.x + decalXNB3, tabHexagone[i].positionGauche.y + decalYNB3, tabHexagone[i].positionGauche.x + decalXND3, tabHexagone[i].positionGauche.y + decalYND3);
			// On renseigne tabApartUV de l'hexagone
			tabHexagone[i].tabApartUV[j] = nbUV;
			
			nbUV++;
			}
			
			//donc l'UV existe déjà
			else
			{
				tabHexagone[i].tabApartUV[j] = chercherUV(tabHexagone[i].positionGauche.x + decalXNG3, tabHexagone[i].positionGauche.y + decalYNG3 );
			}
			
			
			//pour l'UV en bas gauche
			j = 4;
			//test si l'UV n'existe pas déjà
			if( chercherUV(tabHexagone[i].positionGauche.x + decalXNG4, tabHexagone[i].positionGauche.y + decalYNG4 ) == 100 ){
				
			// on créer l'UV	
			tabUV[nbUV] = new UV(tabHexagone[i].positionGauche.x + decalXNG4, tabHexagone[i].positionGauche.y + decalYNG4, tabHexagone[i].positionGauche.x + decalXNB4, tabHexagone[i].positionGauche.y + decalYNB4, tabHexagone[i].positionGauche.x + decalXND4, tabHexagone[i].positionGauche.y + decalYND4);
			// On renseigne tabApartUV de l'hexagone
			tabHexagone[i].tabApartUV[j] = nbUV;
			
			nbUV++;
			}
			
			//donc l'UV existe déjà
			else
			{
				tabHexagone[i].tabApartUV[j] = chercherUV(tabHexagone[i].positionGauche.x + decalXNG4, tabHexagone[i].positionGauche.y + decalYNG4 );
			}
			
			
			//pour l'Uv à l'extrémité gauche
			j = 5;
			//test si l'UV n'existe pas déjà
			if( chercherUV(tabHexagone[i].positionGauche.x + decalXND5, tabHexagone[i].positionGauche.y + decalYND5 ) == 100 ){
				
			// on créer l'UV	
			tabUV[nbUV] = new UV(tabHexagone[i].positionGauche.x + decalXND5, tabHexagone[i].positionGauche.y + decalYND5, tabHexagone[i].positionGauche.x + decalXNB5, tabHexagone[i].positionGauche.y + decalYNB5, tabHexagone[i].positionGauche.x + decalXNG5, tabHexagone[i].positionGauche.y + decalYNG5);
			// On renseigne tabApartUV de l'hexagone
			tabHexagone[i].tabApartUV[j] = nbUV;
			
			nbUV++;
			
			}
			
			//donc l'UV existe déjà
			else
			{
				tabHexagone[i].tabApartUV[j] = chercherUV(tabHexagone[i].positionGauche.x + decalXNG5, tabHexagone[i].positionGauche.y + decalYNG5 );
			}
			
		}
		
	
	
}

/**Fonction qui créé le tableau des controles continus possibles
 * Pour le construire, on va se servir du tableau des UV possible. 
 * On va utiliser une structure similaire à la construction du tableau des UV.
 */
	
protected void ConstructControle(){
	tabControle = new ControleContinu[94];
	
	int nbControle = 0;
	for(int i = 0; i < 19; i++){
		
		/** Cette méthode va consister à créer les COntrole pour chaque hexagone et a remplir un tableau de lien vers ces controle à l'intérieur de cet hexagone.
		 *  Pour chaque controle que l'on créé
		 * on vérifie, en testant les coordonnées des controle, qu'une controle n'a pas déjà été créé. Dans ce cas on
		 * on créé simplement remplit simplement le tableau de l'hexagone par un lien vers controle.
		 */
			
			//Pour le controle en haut
			int j = 0;
			//test si l'UV n'existe pas déjà, pour cela il faut fournir le x et le y de deux point différent car un point peut appartenir à deux rectangles en même temps.
			if( chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionDroit) == 100){
				
			// on créer le controle	
			tabControle[nbControle] = new ControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionDroit, tabUV[tabHexagone[i].tabApartUV[j+1]].positionGauche, tabUV[tabHexagone[i].tabApartUV[j+1]].positionBas, tabUV[tabHexagone[i].tabApartUV[j]].positionBas );
			
			// On renseigne tabAparControle de l'hexagone
			tabHexagone[i].tabApartControle[j] = nbControle;
			
			nbControle++;
			}
			
			//donc le controle existe déjà
			else
			{
				tabHexagone[i].tabApartControle[j] = chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionDroit );
			}
			
			//Pour le controle en haut à droite
			j = 1;
			//test si le controle n'existe pas déjà
			if( chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionDroit) == 100){
				
			// on créer le controle	
			tabControle[nbControle] = new ControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionDroit, tabUV[tabHexagone[i].tabApartUV[j+1]].positionDroit, tabUV[tabHexagone[i].tabApartUV[j+1]].positionGauche, tabUV[tabHexagone[i].tabApartUV[j]].positionBas );
			
			// On renseigne tabApartCOntrole de l'hexagone
			tabHexagone[i].tabApartControle[j] = nbControle;
			
			nbControle++;
			}
			
			//donc le controle existe déjà
			else
			{
				tabHexagone[i].tabApartUV[j] = chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionDroit );
			}
			
			//Pour le controle en bas à droite
			j = 2;
			//test si le controle n'existe pas déjà
			if( chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionGauche.x, tabUV[tabHexagone[i].tabApartUV[j]].positionBas.y ) == 100){
				
			// on créer le controle	
			tabControle[nbControle] = new ControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionGauche, tabUV[tabHexagone[i].tabApartUV[j]].positionBas, tabUV[tabHexagone[i].tabApartUV[j+1]].positionDroit, tabUV[tabHexagone[i].tabApartUV[j+1]].positionGauche );
			
			// On renseigne tabApartcontrole de l'hexagone
			tabHexagone[i].tabApartControle[j] = nbControle;
			
			nbControle++;
			}
			
			//donc le controle existe déjà
			else
			{
				tabHexagone[i].tabApartControle[j] = chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionGauche.x, tabUV[tabHexagone[i].tabApartUV[j]].positionBas.y );
			}
			
			//Pour le controle en bas 
			j = 3;
			//test si le controle n'existe pas déjà
			if( chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionBas) == 100){
				
			// on créer le controle	
			tabControle[nbControle] = new ControleContinu(tabUV[tabHexagone[i].tabApartUV[j+1]].positionDroit, tabUV[tabHexagone[i].tabApartUV[j]].positionGauche, tabUV[tabHexagone[i].tabApartUV[j]].positionBas, tabUV[tabHexagone[i].tabApartUV[j+1]].positionBas );
			
			// On renseigne tabApartcontrole de l'hexagone
			tabHexagone[i].tabApartControle[j] = nbControle;
			
			nbControle++;
			}
			
			//donc le controle existe déjà
			else
			{
				tabHexagone[i].tabApartControle[j] = chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionBas );
			}
			
			//Pour le controle en bas à gauche
			j = 4;
			//test si le controle n'existe pas déjà
			if( chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionGauche) == 100){
				
			// on créer le controle	
			tabControle[nbControle] = new ControleContinu(tabUV[tabHexagone[i].tabApartUV[j+1]].positionDroit, tabUV[tabHexagone[i].tabApartUV[j]].positionDroit, tabUV[tabHexagone[i].tabApartUV[j]].positionGauche, tabUV[tabHexagone[i].tabApartUV[j+1]].positionBas );
			
			// On renseigne tabApartcontrole de l'hexagone
			tabHexagone[i].tabApartControle[j] = nbControle;
			
			nbControle++;
			}
			
			//donc le controle existe déjà
			else
			{
				tabHexagone[i].tabApartControle[j] = chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionGauche );
			}
			
			//Pour le controle en haut à gauche
			j = 5;
			//test si le controle n'existe pas déjà
			if( chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionGauche) == 100){
				
			// on créer le controle	
			tabControle[nbControle] = new ControleContinu(tabUV[tabHexagone[i].tabApartUV[0]].positionGauche, tabUV[tabHexagone[i].tabApartUV[0]].positionBas, tabUV[tabHexagone[i].tabApartUV[j]].positionDroit, tabUV[tabHexagone[i].tabApartUV[j]].positionGauche );
			
			// On renseigne tabApartcontrole de l'hexagone
			tabHexagone[i].tabApartControle[j] = nbControle;
			
			nbControle++;
			}
			
			//donc le controle existe déjà
			else
			{
				tabHexagone[i].tabApartControle[j] = chercherControleContinu(tabUV[tabHexagone[i].tabApartUV[j]].positionGauche );
			}
			
			
		}
	

	
}
	public int getNbJoueur(){
		return nbJoueur;
	}
	
	/** Fonction qui teste à partir de coordonnées ou d'une UV si l'UV existe déjà où que les
	 *  coordonnées appartiennent déjà à une UV. On prendra en entrée, seulement les coordonnée d'un 
	 *  seul point. Cela suffit pour trouver l'UV.
	 *  Return l'Uv si trouvé, retourne null sinon.
	 */
	public int chercherUV( int xE, int yE){
		
		/** on utiliser pour la recherche le for... each ce qui permet, lorsque cette fonction est utilisée
		 * pour la création du tableau des UVs de ne pas avoir à savoir combien le tableau contient déjà d'UV.
		 */
		for(int i = 0; i < 54; i++){
			
			//On teste si x appartient à l'UV
			
			if(tabUV[i] != null){
				if(xE >= ((Math.min(tabUV[i].positionGauche.x , tabUV[i].positionDroit.x) -3) ) && xE <= (Math.max(tabUV[i].positionGauche.x , tabUV[i].positionDroit.x) +3) ){
					
					//On teste si y appartient à l'UV. Pour simplifier, on accepte tout le carrée qui contient l'UV
					if(  yE >= ( Math.min(tabUV[i].positionGauche.y, tabUV[i].positionDroit.y) - 3)  && yE <= (Math.max(tabUV[i].positionBas.y, tabUV[i].positionDroit.y) + 3 ) ){
						return(i);
					}
				}	
			}
		}
		return 100;
	}
	


	/** fonction qui cherche si deux controle continus sont adjacent ou non*/
public boolean isControleAdjacent(ControleContinu controle1, ControleContinu controle2 ){
	
	
	if( longueur(controle1.positionDroitBas, controle2.positionDroitBas) <= 100){
		return true;
	}
	
	return false;
	
}

public boolean verifierCC(int numCC){
	if(tabControle[numCC].status == TypeCC.nonaffiche){
		
		for(ControleContinu controle : tabControle){
			 if( (controle.status != TypeCC.nonaffiche) && (controle.joueur == Jeu.joueurCourant) && (isControleAdjacent(controle,tabControle[numCC]))  ){	
				 return true;
			 }
		 }
			 
		 for(UV uvcherche : tabUV){
			 if( ( (uvcherche.joueur == Jeu.joueurCourant) && (longueur(tabControle[numCC].positionDroitBas, uvcherche.positionBas) <= 30) ) || ( (uvcherche.joueur == Jeu.joueurCourant) && (longueur(tabControle[numCC].positionGauche, uvcherche.positionBas) <= 30) )){
	
				 return true;
			 }
		 }	
	}else
	{
		return false;
	}
	
	return false;
}
public int chercherControleContinu( int xE, int yE){
		

		for( int i = 0; i < 54; i++){
			
			/**On teste si x appartient au controle continu*/
			
			if(tabControle[i] != null){
				if( (  xE >= ( Math.min(Math.min(tabControle[i].positionGauche.x,tabControle[i].positionGaucheBas.x),Math.min(tabControle[i].positionDroit.x,tabControle[i].positionDroitBas.x) ) ) ) && (  xE <= ( Math.max(Math.max(tabControle[i].positionGauche.x,tabControle[i].positionGaucheBas.x),Math.max(tabControle[i].positionDroit.x,tabControle[i].positionDroitBas.x) ) ) ) ){
					
					/**On teste si y appartient à l'UV. Pour simplifier, on accepte tout le carrée qui contient le controle continu*/
					if( (  yE >= Math.min(Math.min(tabControle[i].positionGauche.y,tabControle[i].positionGaucheBas.y),Math.min(tabControle[i].positionDroit.y,tabControle[i].positionDroitBas.y) )  ) &&  (  yE <= Math.max(Math.max(tabControle[i].positionGauche.y,tabControle[i].positionGaucheBas.y),Math.max(tabControle[i].positionDroit.y,tabControle[i].positionDroitBas.y) ) ) ){
						
						/**On va tester si il y a déjà un controle continu à coté*/
						return(i); 
							 
						 
					}
				}	
			}
		}
		return 100;
	}

public int chercherControleContinu( Position P){
	
	/** on utiliser pour la recherche le for... each ce qui permet, lorsque cette fonction est utilisée
	 * pour la création du tableau des UVs de ne pas avoir à savoir combien le tableau contient déjà d'UV.
	 */
	Position position = new Position(P);
	
	for( int i = 0; i < 54; i++){
		
		//On teste si x appartient à l'UV
		
		if(tabControle[i] != null){
			if(position.x >= (tabControle[i].positionGaucheBas.x  -3) && position.x <= ( tabControle[i].positionDroit.x +3) ){
				
				//On teste si y appartient à l'UV. Pour simplifier, on accepte tout le carrée qui contient l'UV
				if(  position.y >= ( tabControle[i].positionDroit.y - 3)  && position.y <= (tabControle[i].positionGaucheBas.y + 3 ) ){
					return(i);
				}
			}	
		}
	}
	return 100;
}
	
	/** Méthode qui distribue les ressources aux joueurs en fonction du lancer de dé */
	public void distribuerRessouces(int lancer){
		
		/**on recherche tout les hexagones qui poduisente et ensuite le UV des joueurs dessus*/
		for( Hexagone hexaNour: tabHexagone){
			if(hexaNour.jeton == lancer){
				
				/**On attribue la ressource pour chaque UV de l'hexagone*/
				for( int i = 0; i < 6; i++){
					/** On teste si l'Uv appartien à quelqu'un*/
					if( tabUV[hexaNour.tabApartUV[i]].status != TypeUV.nonaffiche){
						Jeu.joueurs[tabUV[hexaNour.tabApartUV[i]].joueur].ajouterRessources(hexaNour.production, tabUV[hexaNour.tabApartUV[i]].status);
					}
					
				}
				
			}
		}
		Jeu.InterfaceImp.afficheCarte.repaint();
		Jeu.InterfaceImp.afficheRessource.repaint();
	}
	
	/** Méthode qui permet de bouger le pion noir lors de l'utilisation d'une carte Ancien ou lors d'un 7 aux dés */
	public void bougerPionNoir(){
		UVstatus = 4;
		Jeu.InterfaceImp.repaint();
		Jeu.InterfaceImp.afficheBoutons.afficheRien();
		JOptionPane.showMessageDialog(null,"Cliquer sur la case où le déplacer!", Joueur.getNameFromNum(Jeu.joueurCourant), JOptionPane.INFORMATION_MESSAGE); 
		
		
	}
    
    public boolean bougerPionNoir(int x, int y){
    	for( Hexagone hex: tabHexagone){
    		if( hex.pionNoir = true){
					hex.pionNoir = false;
					
				}
			}
    	for( Hexagone hex: tabHexagone){
			if( ( x >= hex.positionGauche.x) && ( x <= hex.positionGauche.x + 120) ){
				if( ( y >= hex.positionGauche.y - 40) && ( y <= hex.positionGauche.y + 40) ){
					hex.pionNoir = true;
					Jeu.InterfaceImp.afficheBoutons.afficheNormal();
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**Chercher à quel hexagone appartient une UV*/
	public int[] chercherHexagoneUV(int numUV) {
		int k = 0;
		int[] hexagoneTrouve = new int[3];
		for( int j = 0; j < 19; j ++){
			for(int i = 0; i < 6; i++){
				if(tabHexagone[j].tabApartUV[i] == numUV){
					hexagoneTrouve[k] = j;
					k++;
				}
			}
		}
		return hexagoneTrouve;
	}
	
	public int[] chercherHexagoneExtUV(int numUV) {
		int k = 0;
		int[] hexagoneTrouve = new int[3];
		for( int j = 0; j < 18; j ++){
			for(int i = 0; i < 6; i++){
				if(tabHexagoneExt[j].tabApartUV[i] == numUV){
					hexagoneTrouve[k] = j;
					k++;
				}
			}
		}
		return hexagoneTrouve;
	}
	
	/** Renvoit la longueur entre deux points*/
	public int longueur(Position a, Position b){
		return (int) ( Math.rint( Math.sqrt(  Math.abs(a.x - b.x)*Math.abs(a.x - b.x) + Math.abs(a.y - b.y)*Math.abs(a.y - b.y) ) ) );
	}
	
	/**Vérifier qu'il n'y a pas d'autres joueurs autour a moins d'un croisement et qu'il construit */
	public boolean verifierUV(int numUV, TypeUV UV){
		int[] hexagoneTrouve = chercherHexagoneUV(numUV);
		int longueur;
		
		for( int i = 0; i < 3; i++){
			for(int j = 0; j < 6; j++){
				/** Pour ne teste que son type pour sa propre UV*/
				if( tabHexagone[hexagoneTrouve[i]].tabApartUV[j] != numUV ){
					longueur = longueur(tabUV[tabHexagone[hexagoneTrouve[i]].tabApartUV[j]].positionBas, tabUV[numUV].positionBas);
					//on teste si il y a une UV non libre à moins d'un certain distance.	
					if( ( tabUV[tabHexagone[hexagoneTrouve[i]].tabApartUV[j]].status != TypeUV.nonaffiche ) && longueur <= 70 ) {
							System.out.print("passé par longueur");
	
							return false;
						}
				}
			}
		}
		if(UV == TypeUV.uvSimple){
			if( tabUV[numUV].status == TypeUV.nonaffiche ) {
				
				if(!Jeu.debutJ){
				/**On teste si cette UV est adjacent à un CC*/
					for(ControleContinu controle : tabControle){
						 if( (controle.status != TypeCC.nonaffiche) && (controle.joueur == Jeu.joueurCourant) && (longueur(tabUV[numUV].positionBas,controle.positionDroitBas) <= 60 )) {	 
					
							 return true;
						 }
					 }
				}else
				{
					return true;
				}
			}else
			{
				return false;
			}
		}else
		{
			if(  tabUV[numUV].status != TypeUV.uvSimple  ) {
				System.out.print("passé par double");
				return true;
			}else
			{
				return false;
			}
		}
		return false;
	}
	
	/**Pour construire une UV*/
	public boolean construireUV(int x, int y, TypeUV UVd){
		int resultat = chercherUV(x, y);
		if( resultat == 100){
		return false;
		}else
		{
			if( verifierUV(resultat, UVd)) {
				
				tabUV[resultat].status = UVd;
				tabUV[resultat].joueur = Jeu.joueurCourant;
				Jeu.p.UVstatus = 0;
				Jeu.InterfaceImp.afficheBoutons.afficheNormal();
				Jeu.InterfaceImp.repaint();
				return true;
			}else
			{
				return false;
			}
		}
	}
	
	public boolean construireCC(int x, int y){
		int resultat = chercherControleContinu(x, y);
		if( resultat == 100){
		return false;
		}else
		{
			if( verifierCC(resultat) ) {
				tabControle[resultat].status = TypeCC.construit;
				tabControle[resultat].joueur = Jeu.joueurCourant;
				Jeu.InterfaceImp.afficheBoutons.afficheNormal();
				return true;
			}else
			{
				return false;
			}
		}
	}

	/**Fonciton qui calcul la longueur maximal d'un tronçon dont le CC apartient*/
	
	public void longueurMax(ControleContinu cc1){
		for( ControleContinu cc2 : tabControle){
			if(isControleAdjacent(cc1, cc2) && (cc1.joueur == cc2.joueur) && (cc1 != cc2)){
				if(!cc2.compter){
					Jeu.joueurs[cc1.joueur].longueurMaxTemp++;
					cc2.compter = true;
					longueurMax(cc2);
				}
				
			}
		}
	}
	
	/**Fonction pour remettre le comptage à 0*/
	public void annulerCompter(){
		for( ControleContinu cc2 : tabControle){
			cc2.compter = false;
			}
		}
	

/**Fonction pour trouver le joueur qui à la route la plus longue */
public void calculLongueurMax(){
	//on réinitialise la joueur de tout le monde
	for(int i = 0; i < Jeu.nbJoueur ; i++){
		Jeu.joueurs[i].longueurMax = 0;
	}

	for( ControleContinu cc2 : tabControle){
		if(cc2.status == TypeCC.construit){
			Jeu.joueurs[cc2.joueur].longueurMaxTemp = 0;
			annulerCompter();
			longueurMax(cc2);
			if(Jeu.joueurs[cc2.joueur].longueurMaxTemp > Jeu.joueurs[cc2.joueur].longueurMax);
			Jeu.joueurs[cc2.joueur].longueurMax = Jeu.joueurs[cc2.joueur].longueurMaxTemp;
		}
	}
	

	
	//on stocke le précédent joueur
	int joueurprécédent = Jeu.joueurLongueurMax;
	
	Jeu.longueurMax = Jeu.joueurs[0].longueurMax;
	Jeu.joueurLongueurMax = 0;
	for(int i = 1; i < Jeu.nbJoueur ; i++){
		if(Jeu.joueurs[i].longueurMax > Jeu.longueurMax ){
			Jeu.longueurMax = Jeu.joueurs[i].longueurMax;
			Jeu.joueurLongueurMax = i;
		}
	}
	
	if (Jeu.longueurMax > 5){
		if(joueurprécédent != Jeu.joueurLongueurMax){
			JOptionPane.showMessageDialog(null, Joueur.getNameFromNum(Jeu.joueurLongueurMax)+"bravo vous avez la longueur maximale", "Information pour le joueur" + Joueur.getNameFromNum(Jeu.joueurLongueurMax), JOptionPane.INFORMATION_MESSAGE);
	
		}
		
		
		if(joueurprécédent != 10){ 
			Jeu.joueurs[joueurprécédent].cursusPlusLong = false;
			Jeu.joueurs[joueurprécédent].pointVictoire -= 2;
			Jeu.joueurs[Jeu.joueurLongueurMax].cursusPlusLong = true;
			Jeu.joueurs[Jeu.joueurLongueurMax].pointVictoire += 2;
		}
		
	}
}
}