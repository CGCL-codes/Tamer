package Model;

import ElementsProperties.ActivableOnCollision;
import Actors.HumanControlledBall;
import ElementsProperties.Activable;
import ElementsProperties.Dropable;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import Items.*;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author vf
 */
public class Inventory implements PlayerBallListener, CollisionListener {

    Vector<InventoryListener> listeners;

    int playerXIndex;

    int playerYIndex;

    private BufferedImage image;

    private int selectedItemIndex;

    private ArrayList<Item> itemList;

    private boolean inventoryChanged;

    private String message;

    private long messageTimeLeft;

    private long timeByLetter;

    private int currentLetter;

    private int maxShowedLetters;

    public Inventory() {
        listeners = new Vector<InventoryListener>();
        timeByLetter = 5;
        timeByLetter = 15;
        maxShowedLetters = 40;
        selectedItemIndex = 0;
        inventoryChanged = true;
        itemList = new ArrayList<Item>();
    }

    public void addListener(InventoryListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
        listener.inventoryUpdated(new InventoryEvent(this));
    }

    public void clear() {
        itemList.clear();
        selectedItemIndex = 0;
    }

    public BufferedImage getImage() {
        inventoryChanged = false;
        BufferedImage bufferImage = new BufferedImage(640, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics buffer = bufferImage.getGraphics();
        image = bufferImage;
        if (messageTimeLeft > 0) {
            buffer.setColor(Color.WHITE);
            buffer.setFont(new Font("Monospaced", Font.BOLD, 20));
            if (message.length() - currentLetter < maxShowedLetters) buffer.drawChars(message.toCharArray(), currentLetter, message.length() - currentLetter, 150, 37); else buffer.drawChars(message.toCharArray(), currentLetter, maxShowedLetters, 150, 37);
            inventoryChanged = true;
        } else {
            for (int i = 0; (i < 13) && (i < itemList.size()); i++) {
                if (i + selectedItemIndex < itemList.size()) buffer.drawImage(itemList.get(i + selectedItemIndex).getImage(), 155 + i * 36, 16, null); else buffer.drawImage(itemList.get(i + selectedItemIndex - itemList.size()).getImage(), 155 + i * 36, 16, null);
            }
        }
        return image;
    }

    public void addItem(Item item) {
        itemList.add(selectedItemIndex, item);
        inventoryChanged();
    }

    public void scroll() {
        selectedItemIndex++;
        if (selectedItemIndex >= itemList.size()) selectedItemIndex = 0;
        inventoryChanged();
    }

    public void removeItem(Item item) {
        itemList.remove(item);
        inventoryChanged();
    }

    public Item getSelectedItem() {
        if (selectedItemIndex >= itemList.size()) selectedItemIndex = 0;
        if (itemList.isEmpty()) return null; else return itemList.get(selectedItemIndex);
    }

    public void update(long dtime) {
        if (messageTimeLeft > 0) {
            messageTimeLeft -= dtime;
            currentLetter = (int) (message.length() - messageTimeLeft / timeByLetter);
        }
    }

    boolean removeExtraLife() {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i) instanceof Items.ExtraLife) {
                itemList.remove(i);
                inventoryChanged();
                return true;
            }
        }
        return false;
    }

    public void showMessage(String message) {
        this.message = "                 ".concat(message);
        messageTimeLeft = this.message.length() * timeByLetter;
        System.out.println("starting time: " + messageTimeLeft);
        currentLetter = 0;
    }

    public void inventoryChanged() {
        for (InventoryListener listener : listeners) listener.inventoryUpdated(new InventoryEvent(this));
    }

    void useItem() {
        Item item = getSelectedItem();
        if (item instanceof ElementsProperties.Activable) {
            Activable monItem = (Activable) item;
            for (InventoryListener listener : listeners) listener.itemUsed(new ItemUsedEvent(monItem));
        }
        if (item instanceof ElementsProperties.Dropable) {
            this.removeItem(item);
            Dropable myItem = (Dropable) item;
            for (InventoryListener listener : listeners) listener.itemDropped(new ItemDroppedEvent(item, playerXIndex, playerYIndex));
            myItem.droped();
        }
    }

    public void ballMoved(ActorEvent e) {
        playerXIndex = e.getSource().getXIndex();
        playerYIndex = e.getSource().getYIndex();
    }

    public void playerKilled(ActorEvent e) {
    }

    public void collision(CollisionEvent e) {
        if (e.getActor() instanceof HumanControlledBall) {
            if (getSelectedItem() instanceof ActivableOnCollision) {
                ActivableOnCollision activableItem = (ActivableOnCollision) getSelectedItem();
                activableItem.collisionOccured(e);
            }
        }
    }
}
