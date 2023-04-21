package org.tjuscs.sevenwonders.core;

import java.util.*;

/**
 * The Class CardManager.
 */
public class CardManager {

	/** The deck. */
	List<Card> deck;

	/** The guild deck. */
	List<Card> guildDeck;

	/** The discard. */
	LinkedList<Card> discard;

	/** The hand. */
	Hand hand;

	/** The number of players. */
	int numPlayers;

	/**
	 * Instantiates a new card manager.
	 */
	public CardManager() {
		this(7);
	}

	/**
	 * Instantiates a new card manager.<br>
	 * CardManager构造函数
	 * 
	 * @param playerNum
	 *            the player number.玩家人数
	 */
	public CardManager(int playerNum) {
		deck = new ArrayList<Card>(140);
		guildDeck = new ArrayList<Card>(10);
		discard = new LinkedList<Card>();
		hand = new Hand(); // Q: Is this used?
		buildGuildDeck();
		buildDeck();
		numPlayers = playerNum;
	}

	/**
	 * Setup hands.<br>
	 * 设置手牌。
	 * <p>
	 * Get cards we need from the all-no-guild deck according to the age and the
	 * number of players. If it's the 3rd age, also randomly get proper number
	 * of guild cards from the guild deck. At last, shuffle the cards we get,
	 * separate them into different Hands, and return the hands<br>
	 * 根据所处时代和玩家数量从所有非协会牌中取出需要的牌。如果是第三时代，还要随机地从协会牌中取出合适数量的牌。 最后洗牌并分发到不同手牌堆中。
	 * 
	 * @param age
	 *            the age number
	 * @return the hand[]<br>
	 *         设置好的手牌
	 */
	public Hand[] setupHands(int age) {
		ArrayList<Card> ageDeck = new ArrayList<Card>(7 * numPlayers);

		for (Card c : deck) {
			if (c.getAge() == age && c.getplayerNumber() <= numPlayers)
				ageDeck.add(c);
		}

		if (age == 3) {
			shuffle(guildDeck);
			for (int i = 0; i < numPlayers + 2; i++) {
				ageDeck.add(guildDeck.get(i));
			}
		}

		System.out.println(String.format("Age %d has %d cards", age,
				ageDeck.size()));

		shuffle(ageDeck);
		Hand[] hands = new Hand[numPlayers];
		int cardCount = 0;
		for (int plNm = 0; plNm < numPlayers; plNm++) {
			hands[plNm] = new Hand();
			for (int c = 0; c < 7; c++) {
				hands[plNm].add(ageDeck.get(cardCount++));
			}
		}

		return hands;
	}

	/**
	 * Gets the discarded cards.
	 * 
	 * @return the discarded cards
	 */
	public Card[] getDiscardedCards() {
		return discard.toArray(null);
	}

	/**
	 * Discard card.
	 * 
	 * @param card
	 *            the card
	 */
	public void discardCard(Card card) {
		discard.add(card);
	}

	/**
	 * Removes a card from the discarded deck.<br>
	 * 从弃牌堆去除一张牌
	 * 
	 * @param card
	 *            the card.卡牌
	 */
	public void removeFromDiscard(Card card) {
		discard.remove(card);
	}

	/**
	 * Shuffle<br>
	 * 洗牌
	 * 
	 * @param deck
	 *            the deck.牌堆
	 */
	private void shuffle(List<Card> deck) {
		int deckSize = deck.size();
		Card temp;
		int rndLoc;
		Random rndGen = new Random(System.currentTimeMillis());
		for (int iteration = 0; iteration < 2; iteration++) {
			for (int i = 0; i < deckSize; i++) {
				rndLoc = rndGen.nextInt(deckSize);
				temp = deck.get(i);
				deck.set(i, deck.get(rndLoc));
				deck.set(rndLoc, temp);
			}
		}
	}

	/**
	 * Builds the guild deck.<br>
	 * 建立协会牌（紫色牌）<br>
	 * Needs to be a separate deck because only some of them are used each game.<br>
	 * 需要建立一个独立的牌堆，因为其中只有部分牌会在游戏中使用。
	 */
	void buildGuildDeck() {

		// Age III Purple Cards
		Card crd;

		crd = new Card("Workers Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.ORE, 2);
		crd.addCost(Resource.BRICK, 1);
		crd.addCost(Resource.STONE, 1);
		crd.addCost(Resource.WOOD, 1);
		crd.setAction(new ColorBasedVpAction(CardColor.BROWN, 1));
		guildDeck.add(crd);

		crd = new Card("Craftsmens Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.ORE, 2);
		crd.addCost(Resource.STONE, 2);
		crd.setAction(new ColorBasedVpAction(CardColor.GREY, 2));
		guildDeck.add(crd);

		crd = new Card("Traders Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.CLOTH, 1);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.addCost(Resource.GLASS, 1);
		crd.setAction(new ColorBasedVpAction(CardColor.YELLOW, 1));
		guildDeck.add(crd);

		crd = new Card("Philosophers Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.BRICK, 3);
		crd.addCost(Resource.CLOTH, 1);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.setAction(new ColorBasedVpAction(CardColor.GREEN, 1));
		guildDeck.add(crd);

		crd = new Card("Spies Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.BRICK, 3);
		crd.addCost(Resource.GLASS, 1);
		crd.addCost(Resource.WOOD, 1);
		crd.setAction(new ColorBasedVpAction(CardColor.RED, 1));
		guildDeck.add(crd);

		crd = new Card("Strategists Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.ORE, 2);
		crd.addCost(Resource.STONE, 1);
		crd.addCost(Resource.CLOTH, 1);
		crd.setAction(new NeighborDefeatVpAction());
		guildDeck.add(crd);

		crd = new Card("Shipowners Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.WOOD, 3);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.addCost(Resource.GLASS, 1);
		crd.setAction(new ThreeColorBasedVpAction());
		guildDeck.add(crd);

		crd = new Card("Scientists Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.WOOD, 2);
		crd.addCost(Resource.ORE, 2);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.setAction(new FreeSciSymbolAction());
		guildDeck.add(crd);

		crd = new Card("Magistrates Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.WOOD, 3);
		crd.addCost(Resource.STONE, 1);
		crd.addCost(Resource.CLOTH, 1);
		crd.setAction(new ColorBasedVpAction(CardColor.BLUE, 1));
		guildDeck.add(crd);

		crd = new Card("Builders Guild", 3, 3, CardColor.PURPLE);
		crd.addCost(Resource.STONE, 2);
		crd.addCost(Resource.BRICK, 2);
		crd.addCost(Resource.GLASS, 1);
		crd.setAction(new StageVpAction());
		guildDeck.add(crd);

	}

	/**
	 * Builds the deck.<br>
	 * 建立除紫色协会牌外的所有牌
	 */
	void buildDeck() {
		Card crd;

		// Age I brown cards

		crd = new Card("Lumber Yard", 1, 3, CardColor.BROWN);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.WOOD, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);

		deck.add(crd);

		crd = new Card("Stone Pit", 1, 3, CardColor.BROWN);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.STONE, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = new Card("Clay Pool", 1, 3, CardColor.BROWN);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.BRICK, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = new Card("Ore Vein", 1, 3, CardColor.BROWN);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.ORE, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		crd = new Card("Tree Farm", 1, 6, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.WOOD_BRICK, 1);
		deck.add(crd);

		crd = new Card("Excavation", 1, 4, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.STONE_BRICK, 1);
		deck.add(crd);

		crd = new Card("Clay Pit", 1, 3, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.BRICK_ORE, 1);
		deck.add(crd);

		crd = new Card("Timber Yard", 1, 3, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.STONE_WOOD, 1);
		deck.add(crd);

		crd = new Card("Forest Cave", 1, 5, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.WOOD_ORE, 1);
		deck.add(crd);

		crd = new Card("Mine", 1, 6, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.ORE_STONE, 1);
		deck.add(crd);

		// Age II Brown Cards

		crd = new Card("Saw Mill", 2, 3, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.WOOD, 2);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		crd = new Card("Quarry", 2, 3, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.STONE, 2);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		crd = new Card("Brick Yard", 2, 3, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.BRICK, 2);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		crd = new Card("Foundry", 2, 3, CardColor.BROWN);
		crd.addCost(Resource.COIN, 1);
		crd.addGoods(Resource.ORE, 2);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		// Age I & II Grey Cards

		crd = new Card("Loom", 1, 3, CardColor.GREY);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.CLOTH, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(3);
		crd.setAge(2);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = new Card("Glass Works", 1, 3, CardColor.GREY);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.GLASS, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(3);
		crd.setAge(2);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = new Card("Press", 1, 3, CardColor.GREY);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.PAPYRUS, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(3);
		crd.setAge(2);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		// Age I Red Cards

		crd = new Card("Stockade", 1, 3, CardColor.RED);
		crd.addCost(Resource.WOOD, 1);
		crd.addGoods(Resource.SHEILD, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Barracks", 1, 3, CardColor.RED);
		crd.addCost(Resource.ORE, 1);
		crd.addGoods(Resource.SHEILD, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = new Card("Guard Tower", 1, 3, CardColor.RED);
		crd.addCost(Resource.BRICK, 1);
		crd.addGoods(Resource.SHEILD, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		// Age II Red Cards

		crd = new Card("Walls", 2, 3, CardColor.RED);
		crd.addCost(Resource.STONE, 3);
		crd.addGoods(Resource.SHEILD, 2);
		crd.addToFreeList("Fortifications");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Training Ground", 2, 4, CardColor.RED);
		crd.addCost(Resource.WOOD, 1);
		crd.addCost(Resource.ORE, 2);
		crd.addGoods(Resource.SHEILD, 2);
		crd.addToFreeList("Circus");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Stables", 2, 3, CardColor.RED);
		crd.addCost(Resource.ORE, 1);
		crd.addCost(Resource.BRICK, 1);
		crd.addCost(Resource.WOOD, 1);
		crd.addGoods(Resource.SHEILD, 2);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = new Card("Archery Range", 2, 3, CardColor.RED);
		crd.addCost(Resource.ORE, 1);
		crd.addCost(Resource.WOOD, 2);
		crd.addGoods(Resource.SHEILD, 2);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		// Age III Red Cards

		crd = new Card("Fortifications", 3, 3, CardColor.RED);
		crd.addCost(Resource.ORE, 3);
		crd.addCost(Resource.STONE, 1);
		crd.addGoods(Resource.SHEILD, 3);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Circus", 3, 4, CardColor.RED);
		crd.addCost(Resource.STONE, 3);
		crd.addCost(Resource.ORE, 1);
		crd.addGoods(Resource.SHEILD, 3);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("Arsenal", 3, 3, CardColor.RED);
		crd.addCost(Resource.WOOD, 2);
		crd.addCost(Resource.ORE, 1);
		crd.addCost(Resource.CLOTH, 1);
		crd.addGoods(Resource.SHEILD, 3);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Siege Workshop", 3, 3, CardColor.RED);
		crd.addCost(Resource.BRICK, 3);
		crd.addCost(Resource.WOOD, 1);
		crd.addGoods(Resource.SHEILD, 3);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		// Age I Blue Cards

		crd = new Card("Pawnshop", 1, 4, CardColor.BLUE);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.VP, 3);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		crd.setAge(1);
		deck.add(crd);

		crd = new Card("Baths", 1, 3, CardColor.BLUE);
		crd.addCost(Resource.STONE, 1);
		crd.addGoods(Resource.VP, 3);
		crd.addToFreeList("Aqueduct");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		crd.setAge(1);
		deck.add(crd);

		crd = new Card("Altar", 1, 3, CardColor.BLUE);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.VP, 2);
		crd.addToFreeList("Temple");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		crd.setAge(1);
		deck.add(crd);

		crd = new Card("Theater", 1, 3, CardColor.BLUE);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.VP, 2);
		crd.addToFreeList("Statue");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		crd.setAge(1);
		deck.add(crd);

		// Age II Blue Cards

		crd = new Card("Aqueduct", 2, 3, CardColor.BLUE);
		crd.addCost(Resource.STONE, 3);
		crd.addGoods(Resource.VP, 5);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Temple", 2, 3, CardColor.BLUE);
		crd.addCost(Resource.BRICK, 1);
		crd.addCost(Resource.WOOD, 1);
		crd.addCost(Resource.GLASS, 1);
		crd.addGoods(Resource.VP, 3);
		crd.addToFreeList("Pantheon");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("Statue", 2, 3, CardColor.BLUE);
		crd.addCost(Resource.WOOD, 1);
		crd.addCost(Resource.ORE, 2);
		crd.addGoods(Resource.VP, 4);
		crd.addToFreeList("Gardens");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Courthouse", 2, 3, CardColor.BLUE);
		crd.addCost(Resource.BRICK, 2);
		crd.addCost(Resource.CLOTH, 1);
		crd.addGoods(Resource.VP, 4);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		// Age III Blue Cards

		crd = new Card("Pantheon", 3, 3, CardColor.BLUE);
		crd.addCost(Resource.BRICK, 2);
		crd.addCost(Resource.ORE, 1);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.addCost(Resource.CLOTH, 1);
		crd.addCost(Resource.GLASS, 1);
		crd.addGoods(Resource.VP, 7);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("Gardens", 3, 3, CardColor.BLUE);
		crd.addCost(Resource.BRICK, 2);
		crd.addCost(Resource.WOOD, 1);
		crd.addGoods(Resource.VP, 5);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		crd = new Card("Town Hall", 3, 3, CardColor.BLUE);
		crd.addCost(Resource.STONE, 2);
		crd.addCost(Resource.ORE, 1);
		crd.addCost(Resource.GLASS, 1);
		crd.addGoods(Resource.VP, 6);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("Palace", 3, 3, CardColor.BLUE);
		crd.addCost(Resource.STONE, 1);
		crd.addCost(Resource.ORE, 1);
		crd.addCost(Resource.WOOD, 1);
		crd.addCost(Resource.BRICK, 1);
		crd.addCost(Resource.GLASS, 1);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.addCost(Resource.CLOTH, 1);
		crd.addGoods(Resource.VP, 8);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Senate", 3, 3, CardColor.BLUE);
		crd.addCost(Resource.ORE, 1);
		crd.addCost(Resource.STONE, 1);
		crd.addCost(Resource.WOOD, 2);
		crd.addGoods(Resource.VP, 6);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		// Age I Yellow Cards

		crd = new Card("Tavern", 1, 4, CardColor.YELLOW);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.COIN, 5);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("East Trading Post", 1, 3, CardColor.YELLOW);
		crd.addCost(Resource.FREE, 0);
		crd.setAction(new DiscountAction(DiscountAction.DiscountType.RIGHT_RAW));
		crd.addToFreeList("Forum");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("West Trading Post", 1, 3, CardColor.YELLOW);
		crd.addCost(Resource.FREE, 0);
		crd.setAction(new DiscountAction(DiscountAction.DiscountType.LEFT_RAW));
		crd.addToFreeList("Forum");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Marketplace", 1, 3, CardColor.YELLOW);
		crd.addCost(Resource.FREE, 0);
		crd.setAction(new DiscountAction(DiscountAction.DiscountType.BOTH_MANUF));
		crd.addToFreeList("Caravansery");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		// Age II Yellow Cards

		crd = new Card("Forum", 2, 3, CardColor.YELLOW);
		crd.addCost(Resource.BRICK, 2);
		crd.addGoods(Resource.PAPYRUS_GLASS_CLOTH, 1);
		crd.addToFreeList("Haven");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Caravansery", 2, 3, CardColor.YELLOW);
		crd.addCost(Resource.WOOD, 2);
		crd.addGoods(Resource.BRICK_STONE_ORE_WOOD, 1);
		crd.addToFreeList("Lighthouse");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("Vineyard", 2, 3, CardColor.YELLOW);
		crd.addCost(Resource.FREE, 0);
		crd.setAction(new ColorBasedIncomeAction(CardColor.BROWN, 1));
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("Bazar", 2, 4, CardColor.YELLOW);
		crd.addCost(Resource.FREE, 0);
		crd.setAction(new ColorBasedIncomeAction(CardColor.GREY, 2));
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		// Age III Yellow Cards

		crd = new Card("Haven", 3, 3, CardColor.YELLOW);
		crd.addCost(Resource.CLOTH, 1);
		crd.addCost(Resource.ORE, 1);
		crd.addCost(Resource.WOOD, 1);
		crd.setAction(new ColorIncAndVpAction(CardColor.BROWN, 1));
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		crd = new Card("Lighthouse", 3, 3, CardColor.YELLOW);
		crd.addCost(Resource.GLASS, 1);
		crd.addCost(Resource.STONE, 1);
		crd.setAction(new ColorIncAndVpAction(CardColor.YELLOW, 1));
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("Chamber of Commerce", 3, 4, CardColor.YELLOW);
		crd.addCost(Resource.BRICK, 2);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.setAction(new ColorIncAndVpAction(CardColor.GREY, 2));
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("Arena", 3, 3, CardColor.YELLOW);
		crd.addCost(Resource.ORE, 1);
		crd.addCost(Resource.STONE, 2);
		crd.setAction(new StageIncAndVpAction());
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		// Age I Green Cards

		crd = new Card("Apothecary", 1, 3, CardColor.GREEN);
		crd.addCost(Resource.CLOTH, 1);
		crd.addGoods(Resource.COMPASS, 1);
		crd.addToFreeList("Stables");
		crd.addToFreeList("Dispensary");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = new Card("Workshop", 1, 3, CardColor.GREEN);
		crd.addCost(Resource.GLASS, 1);
		crd.addGoods(Resource.COG, 1);
		crd.addToFreeList("Laboratory");
		crd.addToFreeList("Archery Range");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Scriptorium", 1, 3, CardColor.GREEN);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.addGoods(Resource.TABLET, 1);
		crd.addToFreeList("Courthouse");
		crd.addToFreeList("Library");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		// Age II Green Cards

		crd = new Card("Dispensary", 2, 3, CardColor.GREEN);
		crd.addCost(Resource.ORE, 2);
		crd.addCost(Resource.GLASS, 1);
		crd.addGoods(Resource.COMPASS, 1);
		crd.addToFreeList("Arena");
		crd.addToFreeList("Lodge");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		crd = new Card("Laboratory", 2, 3, CardColor.GREEN);
		crd.addCost(Resource.BRICK, 2);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.addGoods(Resource.COG, 1);
		crd.addToFreeList("Siege Workshop");
		crd.addToFreeList("Observatory");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

		crd = new Card("Library", 2, 3, CardColor.GREEN);
		crd.addCost(Resource.STONE, 2);
		crd.addCost(Resource.CLOTH, 1);
		crd.addGoods(Resource.TABLET, 1);
		crd.addToFreeList("Senate");
		crd.addToFreeList("University");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("School", 2, 3, CardColor.GREEN);
		crd.addCost(Resource.WOOD, 1);
		crd.addCost(Resource.CLOTH, 1);
		crd.addGoods(Resource.TABLET, 1);
		crd.addToFreeList("Academy");
		crd.addToFreeList("Study");
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		// Age III Green Cards

		crd = new Card("Lodge", 3, 3, CardColor.GREEN);
		crd.addCost(Resource.BRICK, 2);
		crd.addCost(Resource.CLOTH, 1);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.addGoods(Resource.COMPASS, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(6);
		deck.add(crd);

		crd = new Card("Observatory", 3, 3, CardColor.GREEN);
		crd.addCost(Resource.ORE, 2);
		crd.addCost(Resource.CLOTH, 1);
		crd.addCost(Resource.GLASS, 1);
		crd.addGoods(Resource.COG, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("University", 3, 3, CardColor.GREEN);
		crd.addCost(Resource.WOOD, 2);
		crd.addCost(Resource.GLASS, 1);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.addGoods(Resource.TABLET, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(4);
		deck.add(crd);

		crd = new Card("Academy", 3, 3, CardColor.GREEN);
		crd.addCost(Resource.STONE, 3);
		crd.addCost(Resource.GLASS, 1);
		crd.addGoods(Resource.COMPASS, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(7);
		deck.add(crd);

		crd = new Card("Study", 3, 3, CardColor.GREEN);
		crd.addCost(Resource.WOOD, 1);
		crd.addCost(Resource.PAPYRUS, 1);
		crd.addCost(Resource.CLOTH, 1);
		crd.addGoods(Resource.COG, 1);
		deck.add(crd);

		crd = (Card) crd.clone();
		crd.setplayerNumber(5);
		deck.add(crd);

	} // end of buildDeck method

} // end of CardManager Class
