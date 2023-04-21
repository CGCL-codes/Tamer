package dsc.netgame;

import dsc.util.Queue;

/**
 * Host side chat handler. There should be at least one of these handlers
 * created in the host. Handler gets messages from players and sends
 * new messages to chat targets (usually this means broadcast to all players)
 *
 * @author Dodekaedron Software Creations, Inc. -- Lexa
 */
class Chatter {

    /**
   * Creates a new chatter with empty queues.
   */
    Chatter() {
        q = new Queue();
    }

    /**
   * Checks if there are any messages to be delivered to players.
   * Host should poll this method at some sane frequenzy.
   *
   * @return True if there are messages to be delivered in this chatter.
   */
    boolean isMoreMessages() {
        return (!q.isEmpty());
    }

    /**
   * Gets message to be delivered to some client.
   *
   * @return Next message from the message queue, or null if there
   * are no messages.
   */
    ChatMessage getMessage() {
        return ((ChatMessage) q.remove());
    }

    /**
   * Puts new message into chatter. The message will be handled immediately,
   * and new outgoing messages are put into queue.
   *
   * @param m Message from client.
   */
    void putMessage(ChatClientMessage m) {
        if (m.getSource() >= 0) {
            if (!GlobalDefines.isAccaptableChatString(m.getMessage())) {
                return;
            }
            if (!m.validatePreChar()) {
                return;
            }
        }
        for (int c = 0; c < m.playersTo().size(); c++) {
            if (m.playersTo().get(c)) {
                q.add(new ChatMessage(c, m.getSource(), m.getPreChar() + " " + m.getMessage()));
            }
        }
    }

    /**
   * Queue for outgoing messages.
   */
    private Queue q;
}
